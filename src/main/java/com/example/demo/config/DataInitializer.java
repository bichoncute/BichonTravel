package com.example.demo.config;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.model.Order_item;
import com.example.demo.model.Order_travelers;
import com.example.demo.model.Orders;
import com.example.demo.model.Products;
import com.example.demo.model.Users;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderTravelersRepository;
import com.example.demo.repository.OrdersRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository usersRepository;
    private final OrdersRepository ordersRepository;
    private final OrderItemRepository orderitemRepository;
    private final ProductRepository productRepository;
    private final OrderTravelersRepository orderTravelersRepository;

    public DataInitializer(
    		UserRepository usersRepository,
            OrdersRepository ordersRepository,
            OrderItemRepository orderitemRepository,
            ProductRepository productRepository,
            OrderTravelersRepository orderTravelersRepository) {

        this.usersRepository = usersRepository;
        this.ordersRepository = ordersRepository;
        this.productRepository = productRepository;
        this.orderitemRepository = orderitemRepository;
        this.orderTravelersRepository = orderTravelersRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // =========================
        // 1. 建立初始 User
        // =========================

        Users user;
        if (usersRepository.count() == 0) {
            user = new Users("admin",
                    "admin@demo.com",
                    "ADMIN",
                    "0912345678",
                    "1234",
                    "active"
            );
            Users user2 = new Users("Tom","tom@demo.com","MEMBER","09456456456","1234","active");
            Users user3 = new Users("John",
                    "john@demo.com",
                    "MEMBER",
                    "0912345678",
                    "1234",
                    "active"
            );
            Users user4 = new Users("Mary","mary@demo.com","MEMBER","09456456456","1234","active");
            Users user5 = new Users("Sara","sara@demo.com","MEMBER","09456456456","1234","active");
            user = usersRepository.save(user);
            user2 = usersRepository.save(user2);
            user3 = usersRepository.save(user3);
            user4 = usersRepository.save(user4);
            user5 = usersRepository.save(user5);
            System.out.println("成功建立預設 User！");
            System.out.println("使用的 user_id = " + user.getId());
        } else {

            user = usersRepository.findAll().get(0);
        }


        // =========================
        // 2. 建立初始 Order
        // =========================

//        Orders order;
//
//        if (ordersRepository.count() == 0) {
//
//            order = new Orders();
//
//            order.setUsers(user);
//            order.setTotal_amount(new BigDecimal("10000"));
//            order.setOrder_status("Unpaid");
//            order.setReserved_quantity(1);
//
//            order = ordersRepository.save(order);
//
//            System.out.println("成功建立預設訂單！");
//            System.out.println("使用的 order_id = " + order.getOrder_id());
//
//        } else {
//
//            order = ordersRepository.findAll().get(0);
//        }
     // =========================
        // 3. 建立初始 Products
        // =========================

        Products products1, products2, products3, products4 , products5, products6;

        if (productRepository.count() == 0) {

        		products1 = new Products(
    				    "日本團",
    				    new BigDecimal("50000.00"),
    				    new BigDecimal("60000.00"),
    				    new BigDecimal("10000.00"),
    				    "桃園國際機場",
    				    "東京",
    				    30,
    				    30,
    				    "[商品描述] : ●東京迪士尼樂園：​暢遊充滿夢幻與歡樂的東京迪士尼樂園，感受童話世界的魅力。​\r\n"
    				    + "\r\n"
    				    + "●雲端視角｜SHIBUYA SKY（澀谷天空）展演台 ＆ 晴空塔（Skytree）雙地標：制霸東京天際線：我們同時安排了目前最炙手可熱的 SHIBUYA SKY，站在 360 度無死角的露天展望台上，將著名的「澀谷十字路口」與遠方富士山一覽無遺；更安排登上世界第一高塔東京晴空塔，感受宛如漂浮於雲端的震撼。​\r\n"
    				    + "\r\n"
    				    + "●溫泉療癒：​入住溫泉飯店一晚，讓您在溫泉中放鬆身心，享受純粹的療癒時光。​\r\n"
    				    + "\r\n"
    				    + "●澀谷商圈探索：​漫步東京最繁華的澀谷商圈，自由體驗時尚購物與美食的樂趣。​\r\n"
    				    + "\r\n"
    				    + "●時尚指標｜銀座優雅漫步 ＆ 新宿璀璨不夜城：​購物狂天堂：前往全日本最奢華的銀座（Ginza），朝聖各大國際精品旗艦店與文青百貨 GINZA SIX；夜晚則帶您探索全亞洲最繁華的新宿（Shinjuku），看著最新的 3D 巨型貓咪廣告牆，感受東京最奔放的霓虹生命力。\r\n"
    				    + " ",
    				    "[注意事項] : 費用不含導遊、領隊服務費（每人每天台幣300元）\r\n"
    				    + "不含旅遊平安保險及旅遊不便險等其他私人保險項目\r\n"
    				    + "不含PCR檢測及證明等費用、台灣疫苗施打證明費用\r\n"
    				    + "免簽證",
    				    "TOUR_OPEN",
    				    LocalDate.of(2026, 9, 12),
    				    LocalDate.of(2026, 9, 17)    
    				 );
        		products2 = new Products(
    				    "紐西蘭團",
    				    new BigDecimal("70000.00"),
    				    new BigDecimal("80000.00"),
    				    new BigDecimal("10000.00"),
    				    "桃園國際機場",
    				    "威靈頓",
    				    20,
    				    20,
    				    "[商品描述] : ★魔戒拍攝場景~哈比村之旅:探訪哈比人村、翠綠山丘草原、矮房洞穴等，美景更添奇幻光彩。\r\n"
    				    + "★羅吐魯阿毛利風味餐+傳統舞蹈表演：體驗紐西蘭原住民文化。\r\n"
    				    + "★奧克蘭天空之塔：奧克蘭最具代表之地標，登高望遠，觀賞第一大城全景。\r\n"
    				    + "★紅木樹林：享受森林浴吸收芬多精。\r\n"
    				    + "★懷托摩螢火蟲洞：南半球特有的螢火蟲生態，令人大開眼界‚驚嘆大自然的奧妙。\r\n"
    				    + "★紐西蘭最美渡假勝地~皇后鎮:特別安排兩晚住宿\r\n"
    				    + "★皇后鎮天際纜車+山頂景觀餐廳自助餐：搭乘陡斜的鮑伯峰登山纜車，俯瞰皇后鎮湖光山色美景，並於山頂景觀餐廳享受紐西蘭美食。\r\n"
    				    + "★TSS恩斯洛號古董蒸汽船：暢遊景色秀麗絕美的瓦卡蒂普湖∘\r\n"
    				    + "★瓦爾特峰高原農莊：有趣的農場活動、在湖畔美景的伴隨下享用道地的紐西蘭BBQ。\r\n"
    				    + "★世界級景觀密佛頌峽灣：搭乘遊船暢遊冰河時期被切割沖激成的深谷峽灣、冰河瀑布。\r\n"
    				    + "★Gibbston酒莊：紐西蘭南島著名酒莊，安排品酒+酒莊午餐。\r\n"
    				    + "★蒂卡波湖：呈現乳白藍的夢幻冰河湖，美麗的湖色令人驚艷。",
    				    "[注意事項] : 如有特殊需求者，請務必於報名時主動告知業務人員，讓我們為您提供專業的建議及安排。最遲請在出發前 5 個工作日內臨時提出需求。\r\n"
    				    + "●凡年滿70歲以上或行動不便之貴賓，需有家人或友人同行，方始接受報名，不便之處，尚祈鑑諒。\r\n"
    				    + "●嬰幼兒同行：可能無法妥適兼顧，所以煩請貴賓於報名時，多方考量帶嬰幼兒同行可能產生的不便，以避免造成您的不悅與困擾。\r\n"
    				    + "●未滿18歲者：須與法定代理人一同報名參加旅遊行程，且須透過法定代理人簽署旅遊定型化契約書，報名始為有效，不符報名資格之未成年者，本公司得拒絕受理報名。\r\n"
    				    + "●懷孕旅客：需主動告知懷孕週數及胎數，並隨身攜帶醫師開立註明預產期診斷證明文件，懷孕27週以上(各家航空公司規定週數略有不同，請依照參加的行程所搭乘航空公司之規定)",
    				    "TOUR_OPEN",
    				    LocalDate.of(2026, 8, 1),
    				    LocalDate.of(2026, 8, 12)    
    				 );
        		products3 = new Products(
    				    "韓國團",
    				    new BigDecimal("20000.00"),
    				    new BigDecimal("30000.00"),
    				    new BigDecimal("10000.00"),
    				    "桃園國際機場",
    				    "首爾",
    				    20,
    				    20,
    				    "[商品描述] : 韓國漫遊５天～松島夜景、正東津鐵軌自行車、束草中央市場、雪嶽山國立公園、塗鴉秀、韓屋村(彩妝一站)",
    				    "[注意事項]: 團費不含\r\n"
    				    + "個人因素所產生之消費，如飲料、酒類、廁所清潔費、私人購物費…等。\r\n"
    				    + "同行程所述之自理項目，且未註明之各項開銷，屬額外建議、自費或自由行程所衍生之任何費用。\r\n"
    				    + "同行程所述住宿單人報名指定單人房需補房價差。（或需求由本公司安排與其他同性別旅客同房，此需求恕無法指定與變更，出發後亦無法保證可現場支付價差變更。）\r\n"
    				    + "旅行業責任保險除外之個人旅遊平安險，旅客若有個別需求，得自行另洽保險業務投保旅遊平安保險、醫療險、旅遊不便險。\r\n"
    				    + "本行程派遣領隊／導遊，根據國際旅遊慣例，小費為鼓勵性質，並非費用的一部分。如您對此次服務感到滿意，建議每位旅客每日給予適當小費（建議金額每日 NT$３００元，小孩亦同）。",
    				    "TOUR_OPEN",
    				    LocalDate.of(2026, 9, 1),
    				    LocalDate.of(2026, 9, 12)    
    				 );
        		products4 = new Products(
    				    "加拿大團",
    				    new BigDecimal("120000.00"),
    				    new BigDecimal("13000.00"),
    				    new BigDecimal("10000.00"),
    				    "桃園國際機場",
    				    "溫哥華",
    				    30,
    				    30,
    				    "[商品描述] : 極境逐夢加拿大黃刀鎮極光１０天～保證入住路易絲湖城堡酒店、硫磺山纜車登高一覽、極地原野體驗 ; 追逐極光，找尋幸福的仰望\r\n"
    				    + "找尋幸福的仰望，限量席次即刻預約極光不等人，城堡酒店一房難求！今年冬天，給自己一場最極致的犒賞。與摯愛攜手，踏上這趟極境逐夢之旅，讓歐若拉綠光在您的生命中點亮最永恆的幸福。",
    				    "[注意事項]",
    				    "TOUR_OPEN",
    				    LocalDate.of(2026, 9, 12),
    				    LocalDate.of(2026, 9, 17)    
    				 );
        		products5 = new Products(
    				    "澳洲",
    				    new BigDecimal("70000.00"),
    				    new BigDecimal("80000.00"),
    				    new BigDecimal("10000.00"),
    				    "桃園國際機場",
    				    "雪梨",
    				    15,
    				    15,
    				    "[商品描述]: 🇦🇺 璀璨雪梨、微醺蔚藍：澳洲雪梨經典時尚 7 日── 漫步南半球絕美海港，解鎖歌劇院、藍山與醇香酒莊的英倫優雅你對雪梨的想像，是夕陽下閃耀金光的歌劇院？還是手拿香檳、海風吹拂的遊艇派對？這趟旅程，我們帶你飛往南半球最璀璨的時尚之都 ── 雪梨（Sydney）。這裡有英倫古典的優雅，也有太平洋最奔放的蔚藍。脫下厚重的外套，換上最輕便的墨鏡與洋裝，跟著我們一起在雪梨，感受最寫意的澳式慵懶，找回生活的儀式感。✨ 行程頂級亮點 ── 玩轉雪梨，最懂生活的私房提案🏛️ 經典地標｜雪梨歌劇院（Sydney Opera House）內部深度導覽拒絕走馬看花：不只在廣場拍外觀！我們特別安排專業中文嚮導帶領進入內部，揭開這座 20 世紀最具代表性建築的神秘面紗與震撼音響效果。網美視角解鎖：精選最佳拍攝角度，讓你與雪梨港灣大橋、歌劇院同框，留下驚豔社群的時尚大片。🛳️ 奢華海港｜雪梨港浪漫落日遊船晚宴海上頭等艙體驗：傍晚搭乘豪華雙體遊船，航行在名列世界三大美港的雪梨港。微醺日落饗宴：一邊品嚐澳洲在地主廚料理的精緻三道式晚餐，一邊輕啜澳洲頂級紅白酒。看著夕陽將海面染成粉金，再到華燈初上的百萬夜景，浪漫指數爆表。⛰️ 世界遺產｜藍山國家公園（Blue Mountains）傑卡蘭達與三姐妹岩全景三大纜車體驗：搭乘傾斜 52 度的森林鐵道纜車、高空纜車與索道纜車，360 度俯瞰被尤加利樹芬多精籠罩的藍色山谷。大自然的鬼斧神工：遠眺奇特壯麗的「三姐妹岩」，呼吸南半球最純淨的空氣。🍷 享樂生活｜獵人谷（Hunter Valley）微醺品酒與海灘漫步澳洲最古老酒莊區：前往雪梨後花園 ── 獵人谷，走進如畫般的葡萄園，在專業品酒師帶領下細品享譽國際的希拉（Shiraz）與賽美蓉（Semillon）。邦代海灘（Bondi Beach）潮人打卡：前往衝浪者天堂，踩在細白沙灘上，喝一杯澳式白咖啡（Flat White），甚至去全世最著名的海景泳池 Bondi Icebergs 拍下時尚美照！📅 簡要行程足跡（時尚七日導覽）DAY 01 ｜ 台北 ➔ 舒適客機直飛（或轉機） ➔ 前往陽光南半球DAY 02 ｜ 抵達雪梨 ➔ 皇家植物園（麥覺理夫人椅子眺望雙景） ➔ 海德公園 ➔ 雪梨歌劇院內部導覽 ➔ 晚餐：岩石區英式小酒館DAY 03 ｜ 世界遺產藍山國家公園一日遊（三大纜車、三姐妹岩、蘿拉小鎮英倫風情漫步）DAY 04 ｜ 微醺私房推薦：獵人谷酒莊品酒之旅 ➔ 邂逅野生袋鼠 ➔ 享用酒莊田園午餐DAY 05 ｜ 網美最愛：邦代海灘潮人散策 ➔ 魚市場（Fisherman's Wharf）自選新鮮生蠔與龍蝦海鮮大餐 ➔ 傍晚：雪梨港落日遊船晚宴DAY 06 ｜ 維多利亞女王大廈（QVB）精品購物 ➔ 達令港（Darling Harbour）繽紛夜景 ➔ 前往機場準備返航DAY 07 ｜ 帶著滿滿的維他命 D、時尚美照與微醺回憶，平安返抵國門💌 踏上微醺蔚藍之約，限量席次即刻預約想念海風的溫度了嗎？現在就出發，去雪梨過一個充滿陽光、海鮮與美酒的極致假期。👉 歡迎洽詢專屬旅遊顧問，索取詳細每日行程與早鳥優惠方案！",
    				    "[注意事項] : 當您繳付訂金即表示旅遊契約產生效力，本公司將依各協力商之要求，為您預付此趟旅程的旅館、餐廳或機票等費用。若您因故取消，本公司將依「國外旅遊定型化契約」之相關條款或估算已實付的費用，向您收取超支費用或退回剩餘訂金。多數航空公司一經開立機票，旅客需付全額票款，且不接受調整名單。\r\n"
    				    + "\r\n"
    				    + "如遇有不可抗拒情況，本公司保有變更航空公司，飛行航班及交通工具旅行方式之權利。\r\n"
    				    + "\r\n"
    				    + "本行程使用團體機票，不可加價更改去回程日期、航班時間或延長天數；所示之航班時間，航空公司／機型／時間可能會有所變動。正確航班時間，以團體出發前說明會資料為主。【特別說明】團體機票一經開票後，是不能更改也無法辦理退票，此點基於航空公司之規定，敬請見諒。\r\n"
    				    + "\r\n"
    				    + "",
    				    "TOUR_OPEN",
    				    LocalDate.of(2026, 8, 1),
    				    LocalDate.of(2026, 8, 12)    
    				 );
        		products6 = new Products(
    				    "新馬團",
    				    new BigDecimal("35000.00"),
    				    new BigDecimal("45000.00"),
    				    new BigDecimal("10000.00"),
    				    "桃園國際機場",
    				    "新加坡",
    				    25,
    				    25,
    				    "[商品描述] : 🇸🇬🇲🇾 雙國璀璨、樂遊星馬：新加坡＋馬來西亞經典時尚 5 日── 一次解鎖雙國地標．環球影城、未來阿凡達花園、馬六甲古城與巨型雙星塔不想花大把天數請假，又想擁有出國度假的極致奢華感？這趟旅程，我們帶您一次跨越新馬雙國，感受南洋風情與現代摩天都市的完美交織！從新加坡地表最強的科技魔幻花園，到馬來西亞充滿英葡風情的歷史古城，5 天的精華時間，不用拉車到天荒地老，只有滿滿的驚奇與拍照打卡亮點，給自己和家人一場說走就走的南洋海風假期。✨ 行程頂級亮點 ── 玩轉星馬，最懂安排的高效全覽🪐 未來科技｜新加坡濱海灣花園（Gardens by the Bay）阿凡達現實版：走進宛如外星基地的「冷室花園」與「雲霧林」，看世界最高的室內瀑布傾瀉而下。超級樹幻彩燈光秀：夜幕低降，巨型天空樹隨著音樂亮起璀璨光芒，帶給您極致的視覺震撼。🎢 狂歡尖叫｜聖淘沙新加坡環球影城（Universal Studios Singapore）東南亞唯一：暢玩變形金剛、古埃及、侏羅紀公園等主題區，無論是大人追求刺激還是小孩熱愛卡通，都能在這裡收穫一整天的尖叫與笑容。📜 穿越時空｜馬六甲古城（Melaka）世界文化遺產英葡荷三國風情：漫步在紅色的荷蘭廣場、聖保羅教堂與古老城堡，感受葡萄牙與英國殖民留下的歲月痕跡。雞場街文化散策：體驗熱鬧的華人老街，品嚐正宗海南雞飯粒與娘惹糕點。🌆 時尚巨塔｜吉隆坡國油雙峰塔（Petronas Twin Towers）經典地標打卡：親臨曾經的世界第一高樓，在塔底捕捉最震撼的雙塔同框合影，並在現代化的購物中心享受免稅購物的樂趣。📅 簡要行程足跡（新馬雙國五日精華）DAY 01 ｜ 台北 ➔ 直飛【新加坡】➔ 魚尾獅公園地標合影 ➔ 濱海灣花園（雲霧林＋超級樹夜間幻彩燈光秀） ➔ 入住新加坡時尚飯店DAY 02 ｜ 暢玩一整天：聖淘沙名勝世界 ➔ 新加坡環球影城（保證一票玩到底） ➔ 傍晚專車前往星馬邊境 ➔ 抵達新山DAY 03 ｜ 新山 ➔ 歷史穿越：世界文化遺產【馬六甲古城】（荷蘭紅屋、聖保羅教堂、雞場街、搭乘裝飾人力三輪車） ➔ 前往馬來西亞首都【吉隆坡】DAY 04 ｜ 吉隆坡市區巡禮（獨立廣場、國家清真寺） ➔ 國油雙峰塔（Petronas Twin Towers）打卡 ➔ 黑風洞（彩虹階梯） ➔ 亞羅街夜市（自費品嚐黃亞華烤雞翅、沙爹、貓山王榴槤）DAY 05 ｜ 未來城市：布城（粉紅清真寺、首相府） ➔ 吉隆坡機場 ➔ 帶著滿滿的戰利品與星馬雙國回憶，平安返抵國門🛍️ 舌尖上的星馬 ── 行程必吃南洋美食本行程特別安排在地特色料理，絕不流落街頭挨餓：米其林推薦：正宗新加坡肉骨茶南洋風情：香濃咖哩叻沙麵與招牌娘惹風味餐大馬經典：馬六甲老字號海南雞飯粒💌 跨國雙饗宴，限量席次即刻預約！請假 2 天就能玩雙國！今年最省時、最豐富的度假提案，機位與樂園門票數量有限，提早報名享早鳥優惠。👉 歡迎洽詢專屬旅遊顧問，索取詳細每日行程與即時出團日期！",
    				    "[注意事項] : 團費包含\r\n"
    				    + "團體來回機票（經濟艙）。\r\n"
    				    + "全程飯店住宿（２人一室，單人房需補房差）。\r\n"
    				    + "行程所列之：國外食宿.交通.門票。\r\n"
    				    + "台北、馬來西亞二地機場稅及.燃油附加費。\r\n"
    				    + "旅行業責任保險【意外死殘保額新臺幣２５０萬、意外醫療保額新臺幣２０萬 (實支實付)】。\r\n"
    				    + "旅客未滿１５歲或７０歲以上，依法限制最高【意外死殘保額新臺幣２５０萬元、意外醫療保額新臺幣２０萬（實支實付）】。\r\n"
    				    + "團費不含\r\n"
    				    + "單人房價需補房差。\r\n"
    				    + "旅遊平安保險、醫療險、旅遊不便險\r\n"
    				    + "領隊導遊小費每人每天ＮＴ３００\r\n"
    				    + "費用說明\r\n"
    				    + "含台北、新加坡兩地機場稅及燃油附加費",
    				    "TOUR_OPEN",
    				    LocalDate.of(2026, 9, 5),
    				    LocalDate.of(2026, 9, 14)    
    				 );

        		

        		products1 = productRepository.save(products1);
        		products2 = productRepository.save(products2);
        		products3 = productRepository.save(products3);
        		products4 = productRepository.save(products4);
        		products5 = productRepository.save(products5);
        		products6 = productRepository.save(products6);
            System.out.println("成功建立預設商品！");

        } else {

        		products1  = productRepository.findAll().get(0);
        }
     // =========================
        // 4. 建立初始 Order_item
        // =========================

//        Order_item order_item;
//        
//        if (orderitemRepository.count() == 0) {
//
//        		order_item = new Order_item();
//
//        	  order_item.setProducts(products);
//            order_item.setOrders(order);
//            order_item.setAdult_double_qty(15);
//            order_item.setAdult_single_qty(14);
//            order_item.setInfant_qty(1);
//            
//            order_item.setAdult_double_unit_price(
//            	    products.getAdult_double_price()
//            	);
//
//            	order_item.setAdult_single_unit_price(
//            	    products.getAdult_single_price()
//            	);
//
//            	order_item.setInfant_unit_price(
//            	    products.getInfant_price()
//            	);
//            	BigDecimal subtotal =
//            	        products.getAdult_double_price()
//            	            .multiply(BigDecimal.valueOf(15))
//            	            .add(
//            	                products.getAdult_single_price()
//            	                    .multiply(BigDecimal.valueOf(14))
//            	            )
//            	            .add(
//            	                products.getInfant_price()
//            	                    .multiply(BigDecimal.valueOf(1))
//            	            );
//
//            	order_item.setSubtotal(subtotal);
//            orderitemRepository.save(order_item);
//
//            System.out.println("成功建立預設訂單明細！");
//            System.out.println("使用的 order_id = " + order.getOrder_id());
//
//        } else {
//
//        		order_item = orderitemRepository.findAll().get(0);
//        }

        // =========================
        // 5. 建立初始 Traveler
        // =========================

//        if (orderTravelersRepository.count() == 0) {
//
//            Order_travelers traveler = new Order_travelers();
//
//            traveler.setOrders(order);
//            traveler.setTravelers_real_name("John");
//            traveler.setPassport_num("A123456789");
//            traveler.setPhone_num("0912345678");
//            traveler.setLine_account("john123");
//            traveler.setSpecial_request("");
//            traveler.setTraveler_type("ADULT");
//            traveler.setRoom_type("DOUBLE");
//            traveler.setTraveler_status("ACTIVE");
//
//            orderTravelersRepository.save(traveler);
//
//            System.out.println("成功建立預設旅客資料！");
//            System.out.println(
//                    "使用的 order_id = " + order.getOrder_id()
//            );
//        }
    }
}