package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Orders;
import com.example.demo.model.Payments;
import com.example.demo.service.PaymentService;

@Controller
@RequestMapping("/web/payments")
public class PaymentsController {
	private final PaymentService paymentService;
    
    public PaymentsController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }   
    
    // 顯示款項列表
    @GetMapping
    public String listPayments(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments());
        model.addAttribute("paymentsCount", paymentService.getPaymentsCount());
        return "payments/list";
    }
    
    // 顯示款項詳情
    @GetMapping("/{payment_id}")
    public String getPaymentsDetail(@PathVariable Integer payment_id, Model model) {
        return paymentService.getPaymentsById(payment_id)
                .map(payments -> {
                    model.addAttribute("payments", payments);
                    return "payments/detail";
                })
                .orElse("redirect:/web/payments");
    }
    
    // 顯示付款成功結果
    @GetMapping("/result/success/{payment_id}")
    public String getPaymentsResultSuccess(
            @PathVariable Integer payment_id,
            Model model) {

        return paymentService.getPaymentsById(payment_id)
                .map(payments -> {

                    model.addAttribute("payments", payments);
                    model.addAttribute("isExpired", false);

                    boolean isSuccess =
                            "SUCCESS".equals(payments.getPayment_status());

                    model.addAttribute("isSuccess", isSuccess);

                    return "payments/result";
                })
                .orElse("redirect:/web/payments");
    }
    // 顯示付款失敗結果
    @GetMapping("/result/fail/{payment_id}")
    public String getPaymentsResultFail(@PathVariable Integer payment_id, Model model) {
        return paymentService.getPaymentsById(payment_id)
                .map(payments -> {
                    model.addAttribute("payments", payments);
                    model.addAttribute("isExpired", false);
                    model.addAttribute("isSuccess", false);
                    return "payments/result";
                })
                .orElse("redirect:/web/payments");
    }
    // 顯示付款時間過期結果
    @GetMapping("/result/expired/{payment_id}")
    public String getPaymentsResultExpired(@PathVariable Integer payment_id, Model model) {
        return paymentService.getPaymentsById(payment_id)
                .map(payments -> {
                    model.addAttribute("payments", payments);
                    model.addAttribute("isExpired", true);
                    model.addAttribute("isSuccess", false);
                    return "payments/result";
                })
                .orElse("redirect:/web/payments");
    }
    @PostMapping("/checkout/{payment_id}/expire")
    public String expirePayment(
            @PathVariable Integer payment_id) {

        Payments payment =
                paymentService.expirePayment(payment_id);

        return "redirect:/web/payments/result/expired/"
                + payment.getPayment_id();
    }
    // 顯示建立表單
    @GetMapping("/create/{order_id}")
    public String showCreateForm(@PathVariable Integer order_id, Model model) {

    	Payments payments = new Payments();
        Orders order = new Orders();
        order.setOrder_id(order_id);
        payments.setOrders(order);
        model.addAttribute("payments", payments);
        model.addAttribute("isEdit", false);

        return "payments/form";
    }
    
    // 處理建立表單
    @PostMapping("/create/{order_id}")
    public String createPayments(
            @PathVariable Integer order_id,
            @ModelAttribute Payments payments,
            RedirectAttributes redirectAttributes) {

    			Payments createdPayments =
    					paymentService.createPayments(
                        order_id,
                        payments.getPayment_method(),
                        payments.getPayment_status(),
                        payments.getTransaction_id(),
                        //payments.getAmount(),
                        payments.getStart_at(),
                        payments.getPaid_at()
                );
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "款項建立成功！"
        );
        return "redirect:/web/payments/"
                + createdPayments.getPayment_id();
    }  
    
    
    // 顯示付款頁面
    @GetMapping("/checkout/{order_id}")
    public String checkout(
            @PathVariable Integer order_id,
            Model model) {

        Payments payments = paymentService.startPayment(order_id);

        if ("EXPIRED".equals(payments.getPayment_status())) {

            return "redirect:/web/payments/result/expired/"
                    + payments.getPayment_id();
        }

        model.addAttribute("payments", payments);

        // 傳給 JavaScript 的付款截止時間
        long expiresAtMillis =
                payments.getOrders()
                        .getPayment_expires_at()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli();

        System.out.println(
                "付款截止 timestamp = " + expiresAtMillis
        );

        model.addAttribute(
                "expiresAtMillis",
                expiresAtMillis
        );

        return "payments/checkout";
    }
    
    // 處理使用者確認付款
    @PostMapping("/checkout/{payment_id}")
    public String processCheckout(
            @PathVariable Integer payment_id,
            @ModelAttribute Payments payments) {

        Payments updatedPayment =
                paymentService.confirmPayment(
                        payment_id,
                        payments.getPayment_method()
                );

        if ("EXPIRED".equals(updatedPayment.getPayment_status())) {
            return "redirect:/web/payments/result/expired/"
                    + updatedPayment.getPayment_id();
        }

        return "redirect:/web/payments/result/success/"
                + updatedPayment.getPayment_id();
    }
    // 顯示編輯表單
    @GetMapping("/{payment_id}/edit")
    public String showEditForm(@PathVariable Integer payment_id, Model model) {
        return paymentService.getPaymentsById(payment_id)
                .map(payments -> {
                    model.addAttribute("payments", payments);
                    model.addAttribute("isEdit", true);
                    return "payments/form";
                })
                .orElse("redirect:/web/payments");
    }
    
    // 處理編輯表單
    @PostMapping("/{payment_id}/edit")
    public String updatePayments(@PathVariable Integer payment_id, 
    		@ModelAttribute Payments payments,
    		RedirectAttributes redirectAttributes) {
    		
    	 
    		Integer orderId = (payments.getOrders() != null) ? 
    				payments.getOrders().getOrder_id() : null;
        if (orderId == null) {
            throw new RuntimeException("編輯時必須指定訂單 ID");
        }
        	paymentService.updatePayments(payment_id, orderId,
        			payments.getPayment_method() , payments.getPayment_status() , 
        			payments.getTransaction_id() , payments.getAmount() , 
        			payments.getStart_at() , payments.getPaid_at() 
    			);
        redirectAttributes.addFlashAttribute("successMessage", "款項更新成功！");
        return "redirect:/web/payments/" + payment_id;
    }
    
    // 刪除款項
    @PostMapping("/{payment_id}/delete")
    public String deletePayments(@PathVariable Integer payment_id, RedirectAttributes redirectAttributes) {
        if (paymentService.deletePayments(payment_id)) {
            redirectAttributes.addFlashAttribute("successMessage", "款項刪除成功！");
        }
        return "redirect:/web/payments";
    }
}