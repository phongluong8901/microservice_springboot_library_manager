#--- CQRS và Event Sourcing
CQRS và Event Sourcing là hai mẫu kiến trúc phần mềm thường đi kèm với nhau để xây dựng các hệ thống lớn, phức tạp, đòi hỏi hiệu suất cao và khả năng mở rộng tốt.

1. CQRS là gì? (Command Query Responsibility Segregation)  CQRS là mô hình kiến trúc phân tách rõ ràng giữa hai trách nhiệm:  Command (Luồng Ghi): Chịu trách nhiệm xử lý các thao tác làm thay đổi dữ liệu như Create, Update, Delete (INSERT, UPDATE, DELETE vào database). Luồng này tập trung vào việc kiểm tra business logic (nghiệp vụ) và tính toàn vẹn của dữ liệu.  Query (Luồng Đọc): Chịu trách nhiệm xử lý các thao tác lấy dữ liệu (Read / SELECT) để hiển thị cho người dùng. Luồng này được tối ưu hóa riêng cho việc truy vấn nhanh (có thể dùng cơ sở dữ liệu riêng, cache hoặc các bảng đã được join sẵn).  Lợi ích của CQRS: Giúp hệ thống không bị nghẽn cổ chai khi số lượng người đọc (Query) lớn gấp nhiều lần người ghi (Command), đồng thời giúp code gọn gàng, dễ bảo trì hơn vì tách biệt được logic phức tạp.

2. Event Sourcing là gì?  Trong các ứng dụng thông thường (CRUD), chúng ta thường lưu trạng thái cuối cùng của dữ liệu vào Database (ví dụ: tài khoản hiện tại có số dư là 5 triệu). Nếu có lệnh cập nhật, dữ liệu cũ sẽ bị ghi đè hoặc xóa mất.Event Sourcing hoạt động theo cách khác hoàn toàn:Thay vì lưu trạng thái hiện tại, hệ thống lưu mọi thay đổi dữ liệu (events) dưới dạng một chuỗi các sự kiện xảy ra theo thời gian (Append-only log).  Các sự kiện này bất biến (immutable) – nghĩa là đã lưu vào thì không bao giờ sửa hay xóa.Muốn biết trạng thái hiện tại của một đối tượng ở thời điểm nào đó, hệ thống sẽ phát lại (replay) toàn bộ chuỗi sự kiện từ đầu đến cuối.

3. Khi kết hợp CQRS và Event Sourcing lại với nhau  Mặc dù là hai khái niệm độc lập, CQRS và Event Sourcing sinh ra để dành cho nhau:  Bên phía Command (Ghi): Khi có một hành động xảy ra, hệ sinh thái sẽ sinh ra một Event và lưu nó vào một kho lưu trữ chuyên dụng gọi là Event Store.  Cơ chế đồng bộ: Các sự kiện mới được phát tán đi (thường qua message broker như Kafka, RabbitMQ).  Bên phía Query (Đọc): Lắng nghe các sự kiện đó để cập nhật dữ liệu vào một cơ sở dữ liệu tối ưu riêng biệt (Read Model / Read Database) phục vụ cho việc hiển thị màn hình hoặc tìm kiếm nhanh chóng.

#--- Saga Orchestration
Saga Orchestration là một mô hình thiết kế (design pattern) dùng để quản lý các giao dịch phân tán (distributed transactions) trong kiến trúc microservices.  Khi ứng dụng bị chia nhỏ thành nhiều service (mỗi service có cơ sở dữ liệu riêng), bạn không thể dùng các lệnh commit/rollback truyền thống (như ACID hay Two-Phase Commit) vì sẽ gây khóa tài nguyên và giảm hiệu năng. Mô hình Saga giải quyết bài toán này bằng cách chia giao dịch lớn thành chuỗi các local transaction nhỏ chạy độc lập qua từng service.
1. Bản chất của "Orchestration" trong SagaTrong mô hình Saga, có hai cách điều phối chính là Choreography (phi tập trung, dựa vào sự kiện) và Orchestration (tập trung, có bộ điều phối).Với Saga Orchestration:  Có một đầu mối trung tâm duy nhất được gọi là Orchestrator (có thể là một service riêng biệt hoặc dùng các công cụ như AWS Step Functions, Temporal, Camunda, Netflix Conductor).  Orchestrator này nắm giữ toàn bộ logic nghiệp vụ của tiến trình, chịu trách nhiệm ra lệnh cho các service khác làm gì và theo thứ tự nào. Các service tham gia lúc này trở nên "đơn giản hóa", chỉ việc nhận lệnh, thực thi và trả kết quả về cho Orchestrator.2. Luồng hoạt động thực tế (Ví dụ Đặt hàng - E-commerce)Giả sử một khách hàng bấm nút Đặt hàng, tiến trình bao gồm 3 bước qua 3 service: Order Service, Payment Service, và Inventory Service.  Bắt đầu: Client gửi request đến Order Orchestrator.  Bước 1: Orchestrator gửi lệnh tới Order Service để tạo đơn hàng ở trạng thái Pending.  Bước 2: Khi Order Service báo thành công, Orchestrator tiếp tục gửi lệnh tới Payment Service để trừ tiền.  Bước 3: Nếu trừ tiền thành công, Orchestrator gửi lệnh tới Inventory Service để trừ số lượng kho.  Hoàn tất: Nếu tất cả thành công, Orchestrator cập nhật trạng thái đơn hàng thành Completed.3. Xử lý lỗi và Giao dịch bù (Compensating Transactions)Vì mỗi local transaction commit độc lập vào database riêng của từng service, hệ thống không thể rollback tự động khi có lỗi ở giữa chừng.Tình huống lỗi: Bước 1 và Bước 2 thành công (đã tạo đơn, đã trừ tiền), nhưng đến Bước 3 (Trừ kho) thì thất bại do hết hàng.Cách giải quyết: Orchestrator sẽ phát hiện lỗi và chạy các giao dịch bù (Compensating Transactions - Rollback ngữ nghĩa) theo chiều ngược lại để đưa hệ thống về trạng thái nhất quán:  Gửi lệnh hoàn tiền (Refund) tới Payment Service.Gửi lệnh hủy đơn hàng (Cancel Order) tới Order Service.

#--- email & Kafka consumer


#--- Event Streaming
Event Streaming (Truyền dòng sự kiện) là một mô hình kiến trúc dữ liệu và xử lý thời gian thực, trong đó dữ liệu được ghi nhận, lưu trữ, xử lý và truyền đi liên tục dưới dạng một chuỗi các sự kiện (events) ngay khi chúng vừa xảy ra.

Khác với mô hình lưu trữ tĩnh truyền thống (chờ dữ liệu được gom thành từng đợt - batch rồi mới xử lý), Event Streaming xử lý dữ liệu theo dòng chảy liên tục (giống như dòng nước chảy qua đường ống).

1. Các thành phần cốt lõi trong Event Streaming
Một hệ thống Event Streaming điển hình (như Apache Kafka, Apache Pulsar, hay Redpanda) thường gồm 3 thành phần chính:

Event Producer (Bên phát sự kiện): Các ứng dụng, thiết bị IoT hoặc dịch vụ sinh ra dữ liệu khi có hành động xảy ra. (Ví dụ: Một người dùng bấm nút mua hàng, một cảm biến nhiệt độ ghi nhận số liệu).

Event Broker / Streaming Platform (Hệ thống trung chuyển): Nơi tiếp nhận, lưu trữ an toàn và phân phối các sự kiện. Dữ liệu ở đây thường được sắp xếp thành các chủ đề (Topics) và lưu theo thứ tự thời gian (Append-only log). Khác với hàng đợi (Queue) thông thường, sự kiện sau khi đọc không bị xóa ngay, mà vẫn được lưu giữ trong một khoảng thời gian để các bên khác có thể đọc lại.

Event Consumer (Bên tiêu thụ sự kiện): Các ứng dụng hoặc dịch vụ lắng nghe dòng sự kiện từ Broker để xử lý, phân tích, lưu vào Database hoặc hiển thị lên màn hình.

#--- Event Store va Mesage Router
Trong kiến trúc hướng sự kiện (Event-Driven Architecture) và các hệ thống phân tán, Event Store và Message Router (thường đi kèm với khái niệm Message Routing / Event Routing) đóng vai trò cốt lõi trong việc lưu trữ và điều phối dòng chảy dữ liệu.

1. Event Store (Kho lưu trữ sự kiện)
Event Store là một cơ sở dữ liệu chuyên dụng được thiết kế tối ưu hóa để lưu trữ các sự kiện (events) dưới dạng một chuỗi các bản ghi nối tiếp theo thời gian (append-only log).

Bản chất: Khác với database truyền thống (như MySQL, PostgreSQL) chỉ lưu trạng thái hiện tại và cho phép ghi đè (UPDATE, DELETE), Event Store lưu lại mọi thay đổi đã từng xảy ra trong hệ thống và tính chất của chúng là bất biến (immutable) – nghĩa là dữ liệu đã ghi vào thì không bao giờ sửa hay xóa được.

Vai trò chính:

Lưu trữ cốt lõi cho mô hình Event Sourcing.

Cung cấp khả năng phát lại lịch sử (Replay) để tái tạo trạng thái hệ thống tại bất kỳ thời điểm nào trong quá khứ hoặc phục vụ cho việc debug, phân tích dữ liệu.

Đảm bảo tính toàn vẹn và cung cấp lịch sử kiểm toán minh bạch (Audit Trail) cho các hệ thống tài chính, ngân hàng.

Ví dụ công nghệ: EventStoreDB, Apache Kafka (với cấu hình lưu trữ lâu dài), hoặc các bảng append-only chuyên dụng.

2. Message Router / Event Router (Bộ định tuyến thông điệp/sự kiện)
Message Router là một thành phần trung gian (hoặc một mẫu thiết kế - Enterprise Integration Patterns) có nhiệm vụ kiểm tra nội dung hoặc metadata của một thông điệp/sự kiện đến, sau đó quyết định chuyển tiếp nó đến đúng đích (destination, queue, hoặc topic khác) phù hợp.

Bản chất: Nó giống như một "trạm gác" hoặc "ngã ba đường" giao thông cho dữ liệu. Người gửi (Producer) chỉ cần bắn sự kiện ra một điểm chung mà không cần bận tâm ai là người nhận cuối cùng; Message Router sẽ đọc thông tin (ví dụ: khu vực địa lý, loại giao dịch, mã lỗi) để lọc và phân phối chính xác đến các bên tiêu thụ (Consumers).

Vai trò chính:

Content-Based Routing: Định tuyến dựa trên nội dung bên trong message (Ví dụ: Sự kiện thanh toán có country = 'france' thì rẽ trái sang topic của Pháp, country = 'spain' thì rẽ phải sang topic của Tây Ban Nha).

Giúp các microservices tách rời hoàn toàn (decouple) với nhau, không bị phụ thuộc cứng vào địa chỉ hay endpoint của đối tác nhận.

Ví dụ công nghệ: AWS EventBridge, Apache Kafka Streams (sử dụng TopicNameExtractor), RabbitMQ (Exchange bindings), hoặc các Enterprise Service Bus (ESB) truyền thống.

#--- zookeeper
Apache ZooKeeper là một dịch vụ điều phối tập trung (Centralized Coordination Service) mã nguồn mở, được thiết kế để giải quyết bài toán đồng bộ, quản lý cấu hình và trao đổi thông tin giữa các thành phần trong hệ thống phân tán (Distributed Systems).Hãy tưởng tượng trong một hệ thống gồm hàng chục hoặc hàng trăm máy chủ (nodes) chạy độc lập, việc làm sao để chúng "nhìn nhận" cùng một trạng thái, bầu ra một máy chủ làm nhóm trưởng (leader), hoặc khóa một tài nguyên chung tránh xung đột là cực kỳ phức tạp. ZooKeeper sinh ra để giải quyết "nỗi đau" này.1. Kiến trúc cốt lõi của ZooKeeper  ZooKeeper Ensemble (Cụm ZooKeeper): ZooKeeper thường chạy dưới dạng một cụm gồm nhiều máy chủ (thường là số lẻ như 3, 5, 7) để đảm bảo tính sẵn sàng cao (High Availability). Miễn là đa số các máy trong cụm còn sống (Quorum), hệ thống vẫn hoạt động bình thường ngay cả khi có máy chết.  ZNodes (ZooKeeper Nodes): Dữ liệu của ZooKeeper không lưu trong các bảng quan hệ như SQL, mà được tổ chức theo cấu trúc cây phân cấp giống hệ thống tệp tin (File system) hoặc cây thư mục (ví dụ: /services/app1/config). Các node trong cây này gọi là ZNode. Mỗi ZNode có thể chứa một lượng dữ liệu rất nhỏ (thường là vài KB cấu hình hoặc metadata).  Ephemeral Nodes & Persistent Nodes:Persistent: Dữ liệu lưu vĩnh viễn cho đến khi có lệnh xóa.Ephemeral (Tạm thời): ZNode này sẽ tự động biến mất khi phiên kết nối (Session) giữa client và ZooKeeper bị ngắt (rất hữu ích để kiểm tra xem một service còn sống hay đã chết).Watches (Cơ chế theo dõi): Client có thể gắn một "chiếc còi báo động" (Watch) vào một ZNode nào đó. Khi ZNode đó thay đổi dữ liệu hoặc bị xóa, ZooKeeper sẽ ngay lập tức gửi thông báo real-time về cho client.  ZAB Protocol (ZooKeeper Atomic Broadcast): Giao thức đồng thuận cốt lõi giúp đồng bộ dữ liệu giữa các máy chủ trong cụm ZooKeeper, đảm bảo mọi lệnh ghi đều được thực hiện tuần tự và nhất quán.  

#--- Eureka

# Tìm hiểu về Netflix Eureka trong Microservices

## 1. Vấn đề mà Eureka giải quyết là gì?
Trong hệ thống Monolith (nguyên khối), các module gọi nhau trực tiếp qua tên hàm trong cùng một ứng dụng. Nhưng khi chuyển sang Microservices, hệ thống bị xé nhỏ thành hàng chục, hàng trăm service chạy độc lập trên các địa chỉ IP và cổng (port) khác nhau, và các địa chỉ này có thể thay đổi liên tục (ví dụ khi scale thêm server, khi service bị crash khởi động lại, hoặc khi dùng Docker/Kubernetes).

*   **Nếu không có Eureka:** Service A muốn gọi Service B thì bạn sẽ phải cấu hình cứng (hardcode) IP và Port của Service B vào code của Service A. Khi Service B đổi IP, hệ thống sẽ lỗi.
*   **Khi có Eureka:** Mọi thứ được tự động hóa hoàn toàn thông qua cơ chế danh bạ.

## 2. Eureka hoạt động như thế nào?
Netflix Eureka chia làm 2 thành phần chính:

### Eureka Server (Trung tâm danh bạ)
Là một ứng dụng độc lập làm nhiệm vụ lưu trữ danh sách tất cả các service đang hoạt động trong hệ thống cùng với địa chỉ IP và Port tương ứng của chúng.

### Eureka Client (Các Microservices con)
*   **Service Registration (Đăng ký):** Khi một microservice (ví dụ: Order Service) khởi động lên, nó sẽ tự động gửi thông tin của mình (tên, IP, port) lên cho Eureka Server để "báo danh".
*   **Service Discovery (Khám phá):** Khi Payment Service muốn gọi Order Service, nó không cần biết IP của Order Service là gì. Nó chỉ cần hỏi Eureka Server: "Cho tôi xin địa chỉ của Order Service". Eureka Server trả về danh sách IP khả dụng, và Payment Service sẽ tiến hành gọi.
*   **Heartbeat (Gửi nhịp tim định kỳ):** Các microservices con liên tục gửi tín hiệu "nhịp tim" (heartbeat) cho Eureka Server để xác nhận mình vẫn còn sống. Nếu một service bị sập mà không gửi tín hiệu, Eureka Server sẽ tự động gỡ tên service đó khỏi danh sách để các service khác không gọi vào nữa.

## 3. Ưu điểm nổi bật của Eureka
*   **Loại bỏ cấu hình tĩnh:** Giúp hệ thống linh hoạt, tự động cập nhật khi thêm bớt hoặc thay đổi vị trí các service.
*   **Tích hợp hoàn hảo với hệ sinh thái Java/Spring Cloud:** Chỉ cần thêm thư viện `spring-cloud-starter-netflix-eureka-client` và vài dòng cấu hình là ứng dụng Spring Boot đã có thể tự động kết nối và đồng bộ.
*   **Cơ chế tự bảo vệ (Self-Preservation Mode):** Nếu mạng internet chập chờn khiến Eureka Server tạm thời không nhận được nhịp tim từ hàng loạt client, thay vì xóa sạch các service ra khỏi danh sách, Eureka sẽ bật chế độ bảo vệ để giữ nguyên trạng thái cũ, tránh việc vô tình khai tử các service vẫn đang khỏe mạnh.

#--- Axon Framework
Axon Framework là một Java framework mã nguồn mở mạnh mẽ, được thiết kế chuyên dụng để xây dựng các ứng dụng microservices theo kiến trúc hướng sự kiện (Event-Driven Architecture), đặc biệt là các mô hình nâng cao như CQRS (Command Query Responsibility Segregation) và Event Sourcing (ES).

Nếu bạn đang muốn làm các hệ thống phức tạp, cần lưu lại toàn bộ lịch sử thay đổi (như phân tích ở phần Order/Event Sourcing phía trên), thì Axon chính là "vũ khí tối thượng" trong hệ sinh thái Java/Spring Boot giúp bạn đỡ phải tự code lại từ đầu các cơ chế phức tạp đó.

Các thành phần cốt lõi của Axon Framework:
Commands & Command Handlers (Ghi):

Đại diện cho hành động muốn thay đổi dữ liệu (ví dụ: CreateBookCommand, UpdatePriceCommand).

Command Handler sẽ nhận lệnh, kiểm tra điều kiện (validation) và phát ra các sự kiện.

Events & Event Sourcing (Lịch sử):

Sau khi Command thực thi thành công, một Event (Sự kiện) sẽ được sinh ra và lưu trữ vào Event Store (ví dụ: BookCreatedEvent).

Thay vì lưu một dòng trạng thái cuối cùng, Axon lưu lại toàn bộ chuỗi sự kiện này.

Aggregates (Khối nghiệp vụ gốc):

Là trung tâm xử lý nghiệp vụ. Aggregate chứa trạng thái hiện tại và các quy tắc kinh doanh. Nó nhận lệnh (Command) và sinh ra sự kiện (Event).

Queries & Query Handlers (Đọc):

Tách biệt hẳn với phần ghi, phần này chuyên lắng nghe các sự kiện hoặc nhận yêu cầu truy vấn để trả dữ liệu cho client một cách nhanh chóng (Read Model).

Ưu điểm khi dùng Axon Framework:
Giảm tải code boilerplate: Bạn không cần tự xây dựng cơ chế phát tán Event (Event Bus), quản lý Event Store, hay viết code "replay" lại sự kiện nữa; Axon lo hết.

Tích hợp cực tốt với Spring Boot: Chỉ cần thêm dependency vào Maven/Gradle là có thể cấu hình và chạy ngay.

Mở rộng dễ dàng (Scalability): Dễ dàng tách rời các service, kết hợp với Axon Server để quản lý message routing, tracking tokens và lưu trữ event cực kỳ mượt mà.

--- zooleeper voi kafka
Apache ZooKeeper là một dịch vụ tập trung dùng để duy trì thông tin cấu hình, đặt tên, cung cấp đồng bộ hóa phân tán và quản lý nhóm (group services) cho các hệ thống phân tán lớn.

Trong hệ thống, ZooKeeper hoạt động giống như một "người điều phối" hoặc "thư ký trưởng" giúp các node trong một hệ thống phân tán (cluster) biết được trạng thái của nhau và giữ cho mọi thứ đồng bộ.

Tại sao ZooKeeper lại được dùng với Kafka?
Trước đây, ZooKeeper đóng vai trò cốt lõi trong kiến trúc của Apache Kafka để quản lý và điều phối toàn bộ cluster Kafka. Các nhiệm vụ chính bao gồm:

Quản lý Broker (Broker Coordination): ZooKeeper duy trì danh sách các broker đang hoạt động trong cluster. Nếu một broker bị sập hoặc mất kết nối, ZooKeeper sẽ phát hiện ra và thông báo cho các broker khác.

Bầu chọn Controller (Controller Election): Trong một cluster Kafka, luôn cần một broker đóng vai trò là "Controller" (quản lý việc phân chia partition, xử lý broker lỗi,...). ZooKeeper giúp bầu chọn broker nào sẽ đảm nhận vai trò này.

Quản lý Metadata của Topic: ZooKeeper lưu trữ thông tin về cấu hình của các topic, partition, vị trí đặt replica (bản sao), và danh sách các ACL (Access Control Lists).

Theo dõi Offset (Consumer Offset - các phiên bản cũ): Trước đây, các consumer có thể lưu offset (vị trí tin nhắn đã đọc) trực tiếp lên ZooKeeper (mặc dù hiện nay Kafka đã chuyển sang lưu offset trong chính một internal topic của Kafka).

--- Saga
Trong các giao dịch phân tán (distributed transaction), mô hình Saga có tác dụng giải quyết bài toán đảm bảo tính nhất quán dữ liệu (data consistency) giữa nhiều microservices khác nhau mà không cần dùng đến cơ chế khóa toàn cục (Two-Phase Commit - 2PC) vốn gây chậm hệ thống và giảm hiệu năng.

Dưới đây là các tác dụng cốt lõi và cách hoạt động của Saga:


1. Giải quyết bài toán "Distributed Transaction" mà không cần Two-Phase Commit (2PC)Vấn đề: Trong kiến trúc microservice, mỗi dịch vụ thường có cơ sở dữ liệu riêng. Một nghiệp vụ lớn (như đặt hàng) cần gọi qua nhiều dịch vụ (Kho hàng $\rightarrow$ Thanh toán $\rightarrow$ Vận chuyển). Nếu dùng 2PC, hệ thống phải khóa dữ liệu ở tất cả các nơi cho đến khi mọi thứ hoàn tất, gây nghẽn cổ chai và dễ chết deadlock.Tác dụng của Saga: Thay vì một giao dịch lớn duy nhất, Saga chia nhỏ quy trình thành một chuỗi các giao dịch cục bộ (local transactions) chạy tuần tự hoặc song song ở từng microservice. Mỗi bước hoàn thành sẽ commit dữ liệu độc lập trên database của dịch vụ đó ngay lập tức.

2. Xử lý lỗi và Hoàn tác (Compensating Transactions)Vì các bước trước đã commit dữ liệu độc lập, nếu một bước ở giữa bị lỗi (ví dụ: hết hàng ở kho sau khi tài khoản ngân hàng đã bị trừ tiền), hệ thống không thể "rollback" theo cách thông thường.Tác dụng của Saga: Sử dụng giao dịch bù (compensating transaction). Với mỗi hành động đã thực hiện, Saga định nghĩa một hành động ngược lại để hủy bỏ nó.Ví dụ: Bước 1: Trừ tiền (Thành công). Bước 2: Đặt hàng (Thất bại $\rightarrow$ kích hoạt bù: Hoàn tiền).

3. Đảm bảo tính nhất quán cuối cùng (Eventual Consistency)
Saga chấp nhận đánh đổi tính nhất quán tức thời (strong consistency) để đổi lấy hiệu năng cao và độ mở rộng (scalability) tốt của hệ thống phân tán. Dữ liệu giữa các services có thể lệch nhau trong vài mili-giây hoặc giây, nhưng cuối cùng sẽ đồng bộ (đạt Eventual Consistency).


Hai mô hình triển khai Saga chính:
Choreography (Phi tập trung / Dựa trên sự kiện): Các service tự lắng nghe sự kiện (events) của nhau và tự quyết định bước tiếp theo. Phù hợp với hệ thống nhỏ, ít bước.

Orchestration (Tập trung / Dựa trên điều phối): Có một service trung tâm gọi là Orchestrator đứng ra điều phối toàn bộ luồng đi, gọi service nào trước, service nào sau, và chủ động kích hoạt bù khi có lỗi. Phù hợp với nghiệp vụ phức tạp, nhiều bước.





