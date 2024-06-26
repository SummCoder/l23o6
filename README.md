# l23o6

本仓库用于存储软工二大作业l23o6，是我本科第一次较为系统进行一个软件整体生命周期开发。

## 1. 基本功能 :ballot_box_with_check:

Lab7相关功能：
- 1.客户
    - a. :ballot_box_with_check: 登录、注册、用户管理 
    - b. :ballot_box_with_check: 查询车票、提供搜索、车票细节
    - c. :ballot_box_with_check: 购买车票、选择等级、数量、座位
        - 订单及相关功能实现
        - 用户里程积分实现
        - 里程积分使用
        - 支付策略
    - d. :ballot_box_with_check: 查看订单、展示状态
    - e. :ballot_box_with_check: 退票
- 2.铁路管理员
    - a. :ballot_box_with_check: 登录、注册、用户管理
    - b. :ballot_box_with_check: 管理铁路路线、设置车票信息、更新车次状态
- 3.票务员
    - a. :ballot_box_with_check: 登录、注册、用户管理
    - b. :ballot_box_with_check: 售票、退票
    - c. :negative_squared_cross_mark: 改签
        - 现在才要求，真改不了
- 4.余票管理员
    - a. :ballot_box_with_check: 登录注册、用户管理
    - b. :ballot_box_with_check: 保留票管理
- 5.非功能性检查点
    - a.分布式部署
        - :ballot_box_with_check:已实现：
            - 对系统加以拆分，将不同功能模块拆分为独立服务
            - 设计服务接口，为每个功能模块设计独立的服务接口，定义接口
        - :negative_squared_cross_mark:~~未实现~~：暑假学习了云原生相关技术，devops课程第二次实践利用该项目进行docker容器部署，并实现流水线CICD
            - 部署服务
            - 负载均衡
            - 消息队列redis
    - b. :ballot_box_with_check:安全、信息保护、加密
    - c.响应较快
        - :ballot_box_with_check:将系统按照功能拆分成多个服务
        - :ballot_box_with_check:代码优化：对系统的关键代码进行性能优化，比如减少循环嵌套、减少数据库访问次数、避免重复计算等
- 6.系统质量
    - :ballot_box_with_check:稳定、功能基本满足需求、对可能出现错误进行防护
- 7.额外添加功能实现
    - :ballot_box_with_check:站点及线路保守删除功能
    - :ballot_box_with_check:订单状态随时间的更新
    - :ballot_box_with_check:订单完成后增加积分
    - :ballot_box_with_check:对可能出现错误情况，如余额不足或未打开钱包等的检测
    - :ballot_box_with_check:前端进行美化以及必要功能的添加

## 2. 代码 :ballot_box_with_check:

| **编号**                                 | **检查点**   | **检查点描述**                                               | 备注                                                         |
| ---------------------------------------- | ------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| **Code01**  :ballot_box_with_check:      | 代码结构     | 代码的布局是否能够清晰地体现程序的逻辑结构                   |              根据程序逻辑设计数据与相关动作                                                |
| **Code02**  :ballot_box_with_check:      | 代码命名     | 代码中包、接口、类、方法、变量等的命名是否合理               |                       依据实际含义对类、方法、变量等进行命名                                       |
| **Code03**  :ballot_box_with_check:      | 代码注释     | 文档注释和内部注释，复杂类和方法一定要有注释  |                     配备有相关注释，便于协同开发进行理解                                         |
| **Code04** :ballot_box_with_check: | 代码可靠性   | 是否利用异常与断言进行防御式编程或契约式设计（可选）         | 支付板块采用异常，余额不足或者钱包未打开均会导致支付失败/强制用户选择数值，避免传入空值                                 |
| **Code05** :ballot_box_with_check: | 单元测试代码 | 检查是否有单元测试代码                                       | 在test模块增加各个ServiceTest,进行单元测试                                                   |
| **Code06**  :ballot_box_with_check:      | 集成测试代码 | 是否有桩和驱动代码                                           | 支付策略未采取直接对接，利用socket编程模拟钱包，简单使用桩进行测试                                        |
| **Code07** :ballot_box_with_check:       | 系统架构     | 体系结构设计是否符合某种体系结构风格                         | 分层体系风格，不同层处理各自职能                                                         |
| **Code08** :ballot_box_with_check:       | 接口设计     | 接口设计是否合理，是否通过接口进行调用                       | 完全符合接口分层                                             |
| **Code09** :ballot_box_with_check:       | 控制风格     | 是否使用合适的控制器风格                                     | 分散式风格，高内聚低耦合                                     |
| **Code10** :ballot_box_with_check:       | 设计模式     | 是否合理使用设计模式                                         | 支付方式采用策略模式，减少了耦合 |
| **Code11**​ :ballot_box_with_check:       | 其他         | 架构设计部分是否有其他特点（可选）                           |           实现可拓展                                                   |

## 3. 过程 :ballot_box_with_check:

| 编号                                | 检查点   | 检查点描述                                                   | 备注                                                       |
| ----------------------------------- | -------- | ------------------------------------------------------------ | ---------------------------------------------------------- |
| **Proc01** :ballot_box_with_check:  | 版本控制 | 是否使用版本控制工具，使用工具证明（更新记录截图等）         | 代码开发使用git进行版本控制                                         |
| **Proc02**  :ballot_box_with_check: | 持续集成 | 是否使用持续集成工具，与`Git`，`Maven`等工具整合（自动构建配置，持续集成记录截图等） | 先前利用了课程对应的`DevCloud`平台，后因平台问题废弃使用，改为使用gitlab                         |
| **Proc03**  :ballot_box_with_check: | 团队合作 | 检查团队合作的方式（`SVN`，`Git`等）                         | 使用了`Git`进行协同开发，代码统一上传至gitlab小组仓库中便于协同开发，文档开发协同采用语雀平台                                    |
| **Proc04**  :ballot_box_with_check: | 团队交流 | 检查团队交流方式（会议记录，聊天截图等）                     | 利用`QQ`、`电话`以及线下进行交流，同时使用git备注更新内容，便于所有成员理解                 |
| **Proc05**  :ballot_box_with_check: | 开发模式 | 检查团队开发模式（主程序员，协同开发等）该项与过程得分无关。  注：前来检查过程部分的同学需要说明队员分值（整个大作业，不仅仅是过程）分配，一般来讲为均分，如全部80分，若提出有人工作量多有人工作量少，则按割补法，从工作量少的人扣除分数加到工作量多的人身上，最多10分，如80均分改成70,90,80,80.  如老师发现有队员在团队中贡献过低，将由由老师直接打分。 | 协同开发，各司其职，互相指正修改 |

## 4. 文档 :ballot_box_with_check:

| **编号**                                | **检查点**                                                   | **检查点描述**                                               | 备注           |
| --------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | -------------- |
| Doc01  :ballot_box_with_check:          | 文档完整性                                                   | 包括用例文档、软件需求规格说明文档、体系结构文档、详细设计文档 |                |
| Doc02  :ballot_box_with_check:          | 文档前后一致性                                               | 随意抽一个用例，然后要求在用例文档、需求规格说明文档、体系设计文档、详细设计文档中都有对应的匹配，并且各部分要一致 |                |
| Doc03  :ballot_box_with_check:          | 文档是否纳入配置管理                                         | 各个文档是否有版本号，若有，是否有修订记录                   |                |
| Doc04  :ballot_box_with_check:          | 是否符合技术文档的规范                                       | 是否有清晰的目录结构；是否包含作者、引用文献及引用文档       |                |
| Doc05  :ballot_box_with_check:          | 详细设计文档是否和代码匹配                                   | 抽一个功能点，检查详细设计中的接口（从界面到逻辑到数据访问）和实际代码中的接口是否匹配 |                |
| Doc06  :ballot_box_with_check:          | 需求文档中假设与依赖书写是否正确                             | 查看文档中的依赖和假设书写是否正确                           |                |
| Doc07  :ballot_box_with_check:          | 需求文档中对于功能性需求是否得当                             | 抽取一个需求场景，查看需求文档的相应功能需求描述是否详尽     |                |
| Doc08  :ballot_box_with_check:          | 需求文档中对于非功能需求是否得当                             | 抽取一个需求场景，查看需求文档的相应非功能性需求描述是否详尽 |                |
| Doc09  :ballot_box_with_check:          | 需求文档中用例描述是否等当                                   | 抽取若干用例，查看用例类图、用例描述等是否符合要求           |                |
| Doc10  :ballot_box_with_check:          | 需求文档中非功能需求的约束是否正确 | 查看文档中非功能需求的定义部分，查看约束是否理解为正确约束   |                |
| Doc11  :ballot_box_with_check:          | 需求文档中对于数据需求的定义是否明确 | 查看文档中关于数据的定义部分，查看是否缺少相应数据库的设定   |                |
| Doc12  :ballot_box_with_check:          | 需求文档中假设与依赖书写是否正确                             | 查看文档中的依赖和假设书写是否正确                           |                |
| Doc13  :ballot_box_with_check:          | 需求文档中对于功能性需求是否得当                             | 抽取一个需求场景，查看需求文档的相应功能需求描述是否详尽     |                |
| Doc14  :ballot_box_with_check:          | 架构设计文档中逻辑视图是否规范                               | 抽取其中一个逻辑视图，查看是否满足标准的逻辑过程             |                |
| Doc15  :ballot_box_with_check:          | 架构设计文档中包图是否规范                                   | 抽取其中一部分包图，查看类之间的继承关系、组合关系等是否满足相应的设计模式 |                |
| Doc16  :ballot_box_with_check:          | 架构设计文档中对应的模块功能是否正确                         | 抽取其中若干模块，查看是否满足相应的模块功能                 |                |
| Doc17  :ballot_box_with_check:          | 架构设计文档中使用的设计模式                                 | 查看文档中使用的架构是否搭配相应的设计模式完成相应的任务     |                |
| Doc18  :ballot_box_with_check:          | 架构设计文档中逻辑视图是否规范                               | 抽取其中一个逻辑视图，查看是否满足标准的逻辑过程             |                |
| Doc19  :ballot_box_with_check:          | 架构设计文档中逻辑视图是否规范                               | 抽取其中一个逻辑视图，查看是否满足标准的逻辑过程             |                |
| Doc20  :ballot_box_with_check:          | 架构设计文档中包图是否规范                                   | 抽取其中一部分包图，查看类之间的继承关系、组合关系等是否满足相应的设计模式 |                |
| Doc21  :ballot_box_with_check:          | 详细设计文档等需要指出具体设计细节处是否解释得当             | 抽取一个需求场景，对具体问题进行提问，查看解决方法、异常处理等是否得当 |                |
| Doc22  :ballot_box_with_check:          | 详细设计文档接口描述是否规范                                 | 抽取设计文档中的若干个接口，查看接口描述和接口命名是否规范   |                |
| Doc23  :ballot_box_with_check:          | 详细设计文档中顺序图书写是否规范                             | 抽取一个业务场景，查看顺序图的格式、标号等书写是否满足要求   |                |
| *Doc24*  :ballot_box_with_check:        | 测试用例文档是否完成                                         | 是否完成测试用例文档                                         |                |
| *Doc25*  :ballot_box_with_check: | 测试用例覆盖种类是否齐全                                     | 单元测试，集成测试，系统测试                                 |                |
| *Doc26*  :ballot_box_with_check:        | 测试用例是否描述测试结果                                     | 是否有输入输出，重点关注测试结果的记录                       |                |
