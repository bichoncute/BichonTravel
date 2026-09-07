package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.model.Orders;
import com.example.demo.model.Products;
import com.example.demo.model.Users;
import com.example.demo.repository.OrdersRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OrderItemService;

@SpringBootTest
class HighConcurrencyTest {


@Autowired
private OrderItemService orderItemService;

@Autowired
private ProductRepository productRepository;

@Autowired
private OrdersRepository ordersRepository;

@Autowired
private UserRepository userRepository;

/*
 * 測試設定
 */
private static final int THREAD_COUNT = 100;

// 故意只設定 10 個機位
private static final int INITIAL_CAPACITY = 10;

// 每個 Thread 搶 1 個機位
private static final int SEATS_PER_REQUEST = 1;

private Integer productId;

private final List<Integer> orderIds = new ArrayList<>();


/**
 * 每次測試開始前：
 *
 * 1. 建立一個只有 10 個機位的 Product
 * 2. 建立 100 筆不同的 Order
 *
 * 讓 100 個 Thread 同時搶同一個 Product。
 */
@BeforeEach
void setUp() {

    /*
     * ==========================================
     * 1. 建立測試商品
     * ==========================================
     */

    Products product = new Products();

    product.setName("HIGH CONCURRENCY TEST");

    product.setAdult_double_price(
            new BigDecimal("10000.00")
    );

    product.setAdult_single_price(
            new BigDecimal("15000.00")
    );

    product.setInfant_price(
            new BigDecimal("5000.00")
    );

    product.setDeparture_airport("TEST AIRPORT");
    product.setDestination("TEST DESTINATION");

    product.setMax_capacity(INITIAL_CAPACITY);
    product.setAvailable_capacity(INITIAL_CAPACITY);

    product.setProduct_status("TOUR_OPEN");

    /*
     * 注意：
     * 如果你的 Products.fly_day / back_day
     * 是 nullable = false，就必須設定日期。
     */
    product.setFly_day(
            java.time.LocalDate.now().plusDays(30)
    );

    product.setBack_day(
            java.time.LocalDate.now().plusDays(35)
    );

    Products savedProduct =
            productRepository.saveAndFlush(product);

    productId = savedProduct.getProduct_id();

    System.out.println(
            "\n=========================================="
    );

    System.out.println(
            "High Concurrency Test Product ID = "
            + productId
    );

    System.out.println(
            "Initial Capacity = "
            + savedProduct.getAvailable_capacity()
    );

    System.out.println(
            "Product Version = "
            + savedProduct.getProduct_version()
    );

    System.out.println(
            "==========================================\n"
    );


    /*
     * ==========================================
     * 2. 建立測試用 User
     * ==========================================
     *
     * Order.user_id 是 NOT NULL，
     * 所以 Order 必須有 User。
     *
     * 這裡使用資料庫中第一個 User。
     */

    Users testUser = userRepository.findAll()
            .stream()
            .findFirst()
            .orElseThrow(() ->
                    new RuntimeException(
                            "資料庫中沒有 User，請先建立至少一個會員"
                    )
            );


    /*
     * ==========================================
     * 3. 建立 100 筆不同 Order
     * ==========================================
     *
     * 非常重要：
     *
     * 不讓 100 個 Thread 共用同一筆 Order。
     *
     * 我們真正要測的是：
     *
     * 100 個 Order
     *        ↓
     * 同時搶
     *        ↓
     * 同一個 Product
     */

    orderIds.clear();

    for (int i = 0; i < THREAD_COUNT; i++) {

        Orders order = new Orders();

        order.setUsers(testUser);

        order.setTotal_amount(
                BigDecimal.ZERO
        );

        order.setOrder_status("UNPAID");

        order.setReserved_quantity(0);

        Orders savedOrder =
                ordersRepository.saveAndFlush(order);

        orderIds.add(
                savedOrder.getOrder_id()
        );
    }

    System.out.println(
            "Created test orders = "
            + orderIds.size()
    );
}


/**
 * ==========================================
 * 高併發測試
 * ==========================================
 *
 * 100 個 Thread 同時搶 10 個機位。
 */
@Test
void testHighConcurrency() throws Exception {

    ExecutorService executor =
            Executors.newFixedThreadPool(THREAD_COUNT);

    /*
     * startLatch：
     *
     * 讓 100 個 Thread 全部準備完成後，
     * 再同一時間開始。
     */
    CountDownLatch startLatch =
            new CountDownLatch(1);

    /*
     * doneLatch：
     *
     * 等待所有 Thread 完成。
     */
    CountDownLatch doneLatch =
            new CountDownLatch(THREAD_COUNT);


    /*
     * 統計結果
     */
    AtomicInteger successCount =
            new AtomicInteger(0);

    AtomicInteger failureCount =
            new AtomicInteger(0);

    AtomicInteger optimisticLockCount =
            new AtomicInteger(0);


    /*
     * ==========================================
     * 建立 100 個 Thread
     * ==========================================
     */

    for (int i = 0; i < THREAD_COUNT; i++) {

        final int threadNumber = i;

        executor.submit(() -> {

            try {

                /*
                 * 所有 Thread 在這裡等待
                 */
                startLatch.await();


                /*
                 * 每一個 Thread 使用自己的 Order
                 *
                 * 但所有 Order 都購買：
                 *
                 * 同一個 Product
                 *
                 * 1 個席位
                 */
                Integer orderId =
                        orderIds.get(threadNumber);

                orderItemService.createOrder_items(
                        orderId,
                        productId,

                        // adult_double_qty
                        1,

                        // adult_single_qty
                        0,

                        // infant_qty
                        0
                );


                /*
                 * 如果走到這裡，
                 * 代表這次購買成功。
                 */
                int success =
                        successCount.incrementAndGet();

                System.out.println(
                        "Thread "
                        + threadNumber
                        + " SUCCESS"
                        + " | success = "
                        + success
                );

            } catch (Exception e) {

                /*
                 * 發生例外代表這次沒有成功。
                 */
                failureCount.incrementAndGet();


                /*
                 * 檢查是否為 Optimistic Lock
                 */
                Throwable cause = e;

                boolean optimisticLock =
                        false;

                while (cause != null) {

                    String className =
                            cause.getClass().getName();

                    if (className.contains(
                            "OptimisticLockException")) {

                        optimisticLock = true;
                        break;
                    }

                    cause = cause.getCause();
                }


                if (optimisticLock) {

                    optimisticLockCount.incrementAndGet();

                    System.out.println(
                            "Thread "
                            + threadNumber
                            + " OPTIMISTIC LOCK"
                    );

                } else {

                    System.out.println(
                            "Thread "
                            + threadNumber
                            + " FAILED: "
                            + e.getClass().getSimpleName()
                            + " - "
                            + e.getMessage()
                    );
                }

            } finally {

                doneLatch.countDown();
            }
        });
    }


    /*
     * ==========================================
     * 讓所有 Thread 同時開始
     * ==========================================
     */

    long startTime =
            System.currentTimeMillis();

    startLatch.countDown();


    /*
     * 等待所有 Thread 完成
     */
    boolean completed =
            doneLatch.await(
                    60,
                    TimeUnit.SECONDS
            );

    long endTime =
            System.currentTimeMillis();


    executor.shutdown();

    executor.awaitTermination(
            10,
            TimeUnit.SECONDS
    );


    /*
     * ==========================================
     * 取得最後 Product 狀態
     * ==========================================
     */

    Products finalProduct =
            productRepository.findById(productId)
                    .orElseThrow();


    /*
     * ==========================================
     * 印出測試結果
     * ==========================================
     */

    System.out.println(
            "\n\n"
            + "==========================================\n"
            + "       HIGH CONCURRENCY TEST RESULT\n"
            + "=========================================="
    );

    System.out.println(
            "Threads                : "
            + THREAD_COUNT
    );

    System.out.println(
            "Initial Capacity       : "
            + INITIAL_CAPACITY
    );

    System.out.println(
            "Seats per Request      : "
            + SEATS_PER_REQUEST
    );

    System.out.println(
            "Total Requested Seats  : "
            + (THREAD_COUNT * SEATS_PER_REQUEST)
    );

    System.out.println(
            "Successful Requests    : "
            + successCount.get()
    );

    System.out.println(
            "Failed Requests        : "
            + failureCount.get()
    );

    System.out.println(
            "Optimistic Lock Errors : "
            + optimisticLockCount.get()
    );

    System.out.println(
            "Final Capacity         : "
            + finalProduct.getAvailable_capacity()
    );

    System.out.println(
            "Final Product Version  : "
            + finalProduct.getProduct_version()
    );

    System.out.println(
            "Execution Time         : "
            + (endTime - startTime)
            + " ms"
    );

    System.out.println(
            "All Threads Completed  : "
            + completed
    );

    System.out.println(
            "==========================================\n"
    );


    /*
     * ==========================================
     * 基本驗證
     * ==========================================
     *
     * 最重要：
     *
     * 最後機位不能小於 0。
     */
    assertEquals(
            0,
            finalProduct.getAvailable_capacity(),
            "最後應該剛好沒有剩餘機位"
    );


    /*
     * 成功數量不可以超過初始機位。
     */
    if (successCount.get() > INITIAL_CAPACITY) {

        throw new AssertionError(
                "發生超賣！成功購買數量 = "
                + successCount.get()
                + "，但初始機位只有 "
                + INITIAL_CAPACITY
        );
    }


    /*
     * 總成功人數 + 失敗人數
     * 應該等於 Thread 數。
     */
    assertEquals(
            THREAD_COUNT,
            successCount.get()
            + failureCount.get(),
            "成功 + 失敗數量應該等於 Thread 數量"
    );
}


}
