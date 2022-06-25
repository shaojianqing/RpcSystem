# RPC与路由原理研究性框架说明文档 #
<br />

## 一.总体概述 ##
<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;rpc-system是RPC和路由原理研究性框架。笔者在研究学习公司RPC基础框架与中间件的过程中，为了更深入地理解RPC的原理，掌握复杂网络环境和数据分片部署架构中的路由规则机制，并基于对RPC的整体理解，开发了该研究性框架。在该框架的开发过程中，笔者不限于RPC原理和路由机制的实现。还同时实现了应用组件生命周期管理IOC容器的具体功能，具体实现了Bean组件实例化，组件和Bean设值注入以及初始化方法执行等功能和机制。此外，作为整体框架，亦实现了数据库访问的ORM映射框架与相关组件。具体实现OR映射与操作，Sql映射文件解析，数据源管理，数据库事务管理器以及事务处理模板等功能和机制。从总体上来看，rpc-system框架实现了java应用开发所涉及的大部分基础机制。并以此为基础，实现RPC和路由调用的整体性机制与原理，可以作为RPC和路由原理研究的具体参考实现。
<br />


## 二.基础机制与原理 ##
<br />

### 1.基础IOC容器的实现原理与机制 ###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在RPC与路由原理研究性框架实现的过程中，笔者不仅实现了RPC和路由相关逻辑，作为应用程序整体性的功能，笔者同时实现应用程序组件生命周期管理的基础IOC容器，扮演和Spring相同的角色。基础IOC容器实现经典的Java工厂模式，遵循面向接口设计思想，以实现应用组件之间最大程度的解耦和可配置性。该容器底层基于Java反射机制：通过对类Class信息的反射实例化Java类；通过对方法Method信息的反射执行初始化方法；通过对成员域Field信息的反射进行设值操作等。除上述反射机制以外，框架亦采用DOM4J功能包进行XML配置文件的处理和解析，获取配置文件的各种组件配置信息，并进行相关的验证处理，进而支持应用组件对象的实例化，设值和初始化操作。
<br />

### 2.接口动态代理的实现原理与机制 ###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在RPC调用过程中，服务接口被服务提供方和服务消费方所共享，作为其提供服务和调用服务的共同规约。其中，服务提供方则有服务接口的具体实现类，并实例化为Bean之后，提供具体业务服务。而服务消费方则没有任何服务接口的实现类，那么服务消费方是如何调用服务提供方的服务的呢？刚开始，笔者对此感到非常疑惑，后来在深入研究动态代理机制之后，笔者豁然开朗，并对该种机制获得真切理解。对于服务调用方，在RPC调用过程中，需要解析服务接口参数和返回结果数据，进行路由选择过程，序列化参数和反序列化结果数据，发起网络请求，接收网络响应结果等各种操作。于此同时，上述操作过程对于所有服务接口而言都是完全一致的，并且jdk基础机制提供了动态代理实现，可以通过传入接口数组即可动态创建接口实现类，并实例化其代理对象。调用所有服务接口的方法都会统一交由代理对象的invoke方法执行。基于上述机制和原理，我们可以设计RPC请求通用动态代理类，并在该代理类的invoke方法中实现服务接口方法和参数的解析功能，服务路由功能，参数序列化和返回结果反序列化功能，网络请求和响应处理功能等。由此，我们可以实现RPC调用统一处理逻辑与路由机制。
<br />
### 3.数据库事务管理与事务模板实现机制 ###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;对于基于数据库的应用系统而言，数据库事务是一项非常基础而又重要的数据操作控制机制。因此，作为应用程序底层基础ORM框架，需要提供良好而可靠的数据库事务管理机制以及便捷的事务启动和控制机制。于此同时，Java连接数据库的标准API：JDBC提供了自动事务和手动事务两种基本的事务控制API。因此，基于JDBC手动事务控制方式，即可定义和创建数据库事务管理器TransactionManager和事务模板TransactionTemplate。事务管理器TransactionManager实现beginTransaction，commitTransaction以及rollbackTransaction事务控制方法。而事务模板则实现execute方法，提供事务模板管理的服务。
<br />

## 三.框架组件与结构 ##
<br />
### 1.应用组件生命周期管理IOC容器 ###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在rpc-system整体框架中，rpc.system.core即是其中的应用组件生命周期管理IOC容器。笔者在参考Spring框架原理与实现的基础上，开发完成上述简化的IOC容器。rpc.system.core向其他具体应用提供Bean实例化，依赖注入和初始化方法调用等。其中具体包含BeanFactory接口(用以根据beanId获得Bean实例)，ApplicationContext抽象类(用以解析配置文件并基于解析结果实例化Bean，管理生命周期)，ClassPathXmlApplicationContext实现类(继承自ApplicationContext，主要负责加载xml格式的配置文件，并调用父类功能解析配置和管理Bean)。除上述以外，亦包含相关Bean创建和管理的异常类型，如BeanCreationException异常类。最后，为了方便java反射操作和数据验证等需求实现，rpc.system.core亦包含ClassUtil，AssertUtil，ListUtil和LogUtil类。
<br />
### 2.数据库访问ORM映射组件###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在rpc-system整体框架中，rpc.system.orm即是其中的数据库访问ORM映射组件。笔者同样在参考Mybatis框架原理和实现的基础上，开发完成上述简化的ORM框架。该ORM框架主要实现三部分功能：1.对xml格式的sql映射文件进行解析，将查询参数填入sql语句，以及调用jdbc执行sql语句；2.对sql语句执行之后的结果进行解析，然后生成java数据对象并返回。3.对数据库执行更新操作时，数据库事务管理和事务操作模板管理功能。其中在第一部分功能中，SqlMapConfigParser负责sqlmap文件的解析，并对每一项sql语句生成SqlStatement对象，以及建立对应的映射关系。SqlStatement对象封装一项sql语句的完整操作，以及涉及的各项数据配置内容，如resultDataMap配置，数据源dataSource配置，参数类型parameterType配置和返回类型resultType配置，以及sql语句模板sqlTemplateString配置。此外，在第一部分功能，SqlMapClientTemplate提供上层进行数据库实体操作的所有通用接口，如queryForList，queryForObject，countForObject和execute方法，上述方法包含了数据库实体操作的完整功能接口。在第二部分功能中，数据库操作结果解析功能由DataMapParser类实现。DataMapParser包含两个静态方法：parseObject和parseList方法。parseObject方法用于将单行查询数据解析成单个实体对象，并返回。parseList方法则用于将查询数据解析成实体对象列表，并返回。其中，采用了类反射，成员域反射和java类型判断等原理和机制实现具体的解析过程，具体可以参看prepareDataValue方法和setDataValue方法的实现源码。在第三部分功能中，框架主要由TransactionManager类，TransactionTemplate类以及TransactionCallback接口来实现数据库事务管理的功能。其中，TransactionManager类负责管理数据库事务的具体操作过程，进行事务开启，事务提交，事务回滚等各项操作。因而TransactionManager类提供了事务管理的基础方法：beginTransaction方法用于开启事务，commitTransaction方法用于提交事务，以及rollbacTransaction方法用于回滚事务，最后setInitialCommit方法用于设置初始事务模式。以上即为数据库访问ORM映射组件的整体功能与代码结构说明。
<br />
### 3.RPC路由原理与规则实现组件###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在rpc-system整体框架中，rpc.system.route即是其中的RPC路由原理与规则实现组件。对于服务调用端而言，该组件具体实现调用端动态接口的处理逻辑，路由规则与接口访问路由功能以及接口参数和返回结果的序列化，异常信息解析等功能。而对于服务提供端而言，该组件则实现服务的发布，调用请求数据和服务结果的解析等序列化功能。在调用端调用服务的处理过程中，框架采用接口动态代理的机制，将所有的服务接口调用转移到RpcProxy实例的invoke方法中，而RpcProxy的invoke方法则实现所有服务接口调用的功能，具体可以分为parameter参数处理，路由标志位解析，服务元数据获取，远程服务地址路由和调用请求上下文构建，调用请求上下文序列化，发出网络请求，接收返回结果，反序列化并返回结果共8个步骤。以下详细说明其中各种类和对象设计内容。RpcProxy类实例用于实现动态接口的所有具体功能，包括数据序列化，路由映射，网络访问等。ServiceMetaDataFactory类实例用于解析服务配置文件中的<rpc:reference />标签，并生成相应的ServiceMetaData实例，用以在发起RPC调用的时候，提供服务元数据配置。ServiceMetaData共包含targetUrl和vipOnly两项配置数据，路由规则将会据此判断和计算最终路由的url地址。RouteRule注解类用于配置服务接口对应UserId的最后两位路由标志位的解析类，这样在发起服务接口调用时，框架就可以据此反射生成该解析类实例，并调用该解析类实例的generateRule方法即可计算获得路由标志位，以进行路由计算过程。与RouteRule类相配套的是RouteRuleProcessor类实例，该类实例用于解析服务接口或接口方法上面的RouteRule注解配置，并返回路由标志位。在上述功能类的基础之上，RouteRuleMapper类则执行具体的路由计算逻辑和规则，具体在routeRpcContext方法中实现VIP路由，软负载路由和Proxy代理路由共三种路由方式，由此实现同Zone，跨Zone同机房，跨机房同城，跨城共四种场景的RPC调用。除上述核心功能以外，由于RPC路由过程需要涉及软负载中心，服务代理，统一部署管理中心等管控设施，因此RouteRuleMapper类亦实现各种初始化方法，如initProxyConfig，initConfregMapper，initRouteRuleMapper和initVipAddressMapper等方法，用以初始化上述各项设施相关的基础配置数据等。RpcServiceContext类实例则负责加载和运行服务提供方的服务接口及其实现类实例，以此，服务提供方即可向服务消费方提供RPC服务。RpcReferenceContext类实例则用于加载和初始化服务接口配置元数据信息，各类服务管控设施配置数据初始化等职责，以此，服务消费方即可向服务提供方发起RPC服务调用。另外，还有ConfregClient类负责在服务提供方启动服务的时候，向Confreg配置中心注册其提供的服务接口信息。以及ZoneContextUtil类负责初始化本应用运行所在的具体zone配置信息。最后，路由组件还包括相关数据表达和关联类，以下列表形式说明:

数据类名 | 功能与职责说明 
-------|------
ResultData|服务端进行业务处理之后，包装成对应数据对象的通用结果数据类，用以包装和组合服务提供方返回的结果数据。ResultData类包含status状态，data数据字符串(以Json形式表示的数据)，message业务处理消息，以及exceptionClass异常类全类名。其中，status状态表示此次调用请求是成功，还是失败；data表示具体的处理结果数据；message则表示业务处理中出现的消息，比如异常信息等；而exceptionClass异常类全类名则表示服务提供抛出的异常类型，用以在客户端进行反射生成异常类实例，并抛出异常。这样客户端就可以感知服务端的异常信息了，在这里主要是业务异常。
RpcContext|Rpc调用上下文对象，包含一次Rpc调用所需要的全部配置数据和调用请求数据。其中包含serverUrl服务地址，proxyUrl代理地址，isProxied是否使用代理标志，appName应用名，serviceName服务名，methodName方法名，identifier唯一标识名，traceId追踪标记，parameters参数列表共9项配置数据。
ParameterData|Rpc调用参数表示对象，标识Rpc调用所涉及的参数数据。ParameterData在RpcContext中以List的方式顺序从左至右标识接口方法的调用参数列表。ParameterData实例包含一个type类型和value数据，type类型标识参数类型的全类名，用以反射方式在服务提供方生成参数实例，而value值即为参数以json表达的具体值。
UUIDGenerator|UUID生成器，用以在服务调用端生成uuid值，并作为服务的traceId值，以追踪和调试RPC请求服务的各种细节信息。
AddressInfo|表示服务提供方将自身提供的服务配置和元数据注册到配置中心所需要的地址配置信息。内含appName应用名，interfaceName接口名，methodName方法名，identifier服务唯一标识名，以及ipAddress具体的访问IP地址。
ZoneInfo|表示一个Zone的部署配置信息，具体包含name名称，idcName机房名称，cityName城市名称，rpcUrl访问Url配置，以及range用户UserId对应的范围。

最后显示RPC路由处理流程图如下所示：
![image](https://github.com/shaojianqing/RpcSystem/blob/master/images/rpcSchedul.png)

### 4.通用工具类实现组件###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在rpc-system框架结构中，rpc.system.util即为其通用工具类实现组件。该组件包含各项基础通用的工具类，并提供给其他框架和业务组件基础的工具性功能服务。以下列表说明各个工具类及其相关的接口的具体功能职责和使用说明。

工具类 | 功能与职责说明
------|--------------
IdentifierUtil|该类通过反射的方式获得method的名称，所在类/接口的名称，参数列表数据，并基于此生成服务接口内一个业务方法的全局唯一的标识名。该名称用于在rpc服务发布，注册和发现时，进行服务接口方法的标识和映射。该类具体方法为String generateIdentifier(Method method)。
NetworkUtil|该类是网络访问的基础工具类，用于发起网络请求和接收网络响应，在框架中所有涉及网络访问的地方都采用该类进行网络访问操作。该类底层直接采用Java Socket进行网络通信，使用IP/TCP协议保证数据连接的特性，但是通信效率偏低，这也是以后可以进行优化的地方。具体进行网络访问的方法是String sendRequest(String serverIp, int port, String content)。
ProtocolUtil|该类是进行数据序列化和反序列化的数据交换协议工具类。现在该类仅以Json为数据交换协议，进行对象的协议转换操作，用于RPC请求和响应数据的转换操作中。其中具体的转换方法为:<T> T transToObject(String data, Class<T> clazz)和String transToJson(Object object)方法。
ServerUtil|该类是服务提供方对外提供TCP服务的工具类，内部采用ServerSocket类来实现服务端监听Socket对象。具体启动TCP监听服务的方法为void accept(int port, final RequestCallBack callBack)方法，该方法需要传入一个RequestCallBack类型的回调接口，用以在接收到网络请求时，回调相应的请求处理器进行网络请求的处理，并在之后，返回相应的处理结果。
ThreadUtil|该类为线程启动工具类，用以启动一个新线程。由于网络请求监听和处理模型中，异步多线程是一个常用的运行模型(比如Tomcat服务器即采用这种运行模型)，因此ThreadUtil工具类用于在服务提供方监听到网络请求后，启动一个新线程去处理和执行网络请求。

以上即为rpc框架中主要的工具类说明，具体细节请参阅相关实现代码。
<br />
### 5.Zone部署信息管理组件ZoneManager ###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ZoneManager组件在rpc-system框架结构中扮演统一配置和管理ZoneInfo部署信息的管控中心角色。其具体的应用为rpc.system.manager。在该研究性RPC框架中，ZoneManager包含的功能与职责角色较为简单，仅负责向服务消费方提供Zone部署配置信息。具体包含ZoneManager类和ZoneConfigFactory两个功能类，其中ZoneManager类负责启动TCP监听服务，以监听和接收服务消费方发出的服务请求，并向其发送服务响应数据；而ZoneConfigFactory类则负责配置和输出Zone部署配置信息。ZoneConfigFactory类目前是静态配置所有Zone的部署配置信息，暂无管控端应用和操作组件。
<br />
### 6.软负载配置中心管理组件Confreg ###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Confreg组件在rpc-system框架结构中则扮演服务注册与配置中心的角色。其具体的应用为rpc.system.confreg模块，内部包含ConfregServer类和ConfregMapper类。ConfregServer类负责监听服务注册和发现请求，返回注册结果和服务配置信息等。ConfregMapper类则负责保存和输出服务注册信息。上述机制和设施中，暂无服务注册信息的管控端，以及与服务提供方和服务发现方进行长连接机制以保证服务状态的动态更新和管控机制等。
<br />
### 7.跨城接口访问代理组件Proxy ###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Proxy组件在rpc-system框架结构中扮演转发请求代理的角色。用于跨城或者是跨域RPC请求场景中，将服务调用方的请求代理转发到服务提供方。Proxy组件的工程名为rpc.system.proxy，其中包含代理工具类ProxyUtil，用以实现主体代理功能与机制；代理实例类ProxyInstance，用以启动代理服务和输出各种日志等；以及ProxyCallBack接口，用以提供代理数据和日志监控的模板接口。
<br />
### 8.域名映射与应用状态管理组件Vip ###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Vip组件在rpc-system框架结构中扮演IP地址与域名映射，以及流量管控的功能(流量管控暂未在此框架中实现，但在功能规划与设计中，包含流量管控的功能设计)。由于现有的实现仅包含IP地址与域名映射功能，因此，Vip组件包含的代码并不多。在rpc-system框架结构中，rpc.system.vip即为VIP组件的工程名，内部包含VipServer类，用以启动vip域名映射与转换服务；VipAddressMapper类，具体包含域名映射数据和映射逻辑。
<br />
### 9.服务相关实体类组件 ###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在RPC框架整体结构中，rpc.system.domain组件即为其中的实体类及实体配置文件组件。其中包含UserInfo，AccountInfo，AccountBill，Sequence和OperateRecord共5个实体类。UserInfo是用户信息；AccountInfo是账户信息，与用户信息是一对一的关联关系；AccountBill则是账单信息，表示一次账单操作；Sequence是数据表主键序列记录，统一维护所有其他数据表的主键生成记录；最后OperateRecord是操作记录信息，表示一次用户操作记录。上述5个实体类均继承自Base基类，内含id，isValid，createTime和operateTime四个基础数据字段。最后则是上述5个实体类对应的数据库操作映射文件：UserInfo.xml，AccountInfo.xml，AccountBill.xml，Sequence.xml以及OperateRecord.xml。上述java类和配置文件构成服务相关实体类组件的整体结构与内容。
<br />
### 10.服务调用接口定义组件 ###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;rpc.system.interface组件即是RPC框架与示例中的服务调用接口定义组件。其中定义了三个服务接口：IUserInfoService用户信息服务接口；IAccountInfoService账户服务接口；IAccountBillService账单服务接口。以及对应的uid解析器实现类UserInfoIdGenerator和AccountBillUidGenerator，以用于实现uid的解析逻辑功能。此外，亦包含账单操作请求包装类AccountBillOperRequest，用以传递账单操作的请求参数等。该服务接口组件同时被服务提供者rpc.system.provider和服务消费者rpc.system.consumer所共同引用，并作为两者之间rpc通信的一个接口规约。在服务提供者组件中，有相应的服务实现类实现对应服务逻辑；在服务消费者组件中，则通过Java动态代理机制根据服务接口定义，在内存中实例化服务代理类实例，并通过RpcProxy代理实现RPC调用的所有处理功能，如路由解析，参数序列化与返回结果反序列化，以及异常处理等。
<br />
### 11.服务提供者实现组件 ###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; rpc.system.provider组件是RPC框架与示例中的服务提供者实现组件。其中实现了rpc.system.interface服务接口定义组件中定义的三个业务接口的具体逻辑与功能。rpc.system.provider使用rpc.system.core组件中实现的IOC容器机制，实例化并管理所有服务对象，同时采用rpc.system.orm组件中实现的ORM实体关系机制，进行数据库的各种操作，以及各种事务处理等，具体实现逻辑请参看相关源码。
<br />
### 12.服务消费者实现组件 ###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; rpc.system.consumer组件是RPC框架与示例中的服务消费者实现组件。其中调用rpc.system.interface服务接口定义组件中定义的三个业务接口。并在各自的Action操作类中进行接口的各种调用和操作逻辑，具体包含UserInfoAction用户信息操作类；AccountInfoAction账户信息操作类；AccountBillAction账单数据操作类。上述操作类均在ClientInstance类中进行实例化并执行接口调用逻辑。
<br />

## 四.框架发展路线 ##
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 由于该RPC框架仅作为笔者研究SOFA-RPC机制与原理过程中，学习和理解内部深入机制之用。因此，该RPC框架在设计和结构上都相对简化，虽能很好的体现各种技术原理和实现，但依然存在大量不足和完善的空间。有待于各位有兴趣的同学一起加入并持续完善。以下，笔者根据自己的理解，列出该RPC框架可以继续发展和完善的若干点路线与方向：
<br />
1. 根据通信协议的适用场景和具体要求，选择更为高效和灵活的通信协议。当前RPC框架中所采用的通信协议为基于文本的JSON数据交换协议，并采用谷歌的GSON进行序列化和反序列化处理，但是未经过GZip压缩处理。因此，就数据流量和通信效率而言，整体性能和效率显得较为低下。在此，可以考虑换用谷歌的二进制通信协议Protocol Buffers，或者其他在性能，效率，兼容性和可扩展性等各方面具有良好表现的二进制通信协议。
<br />
<br />
2. 选择具有缓存的IO机制与设施等，比如Buffered IO或NIO等。当前RPC框架中所采用的IO模型为线程加同步IO模型，IO本身并没有实现多路复用机制，因此整体IO吞吐效率并不高，IO通道存在较大浪费的问题。在正式生产环境中，如果要进一步提高效率和性能，需要采用一种新的，最好是实现IO多路复用机制的IO模型与机制，比如，在Linux平台上面，可以考虑采用epoll机制，该机制被公认为服务器IO模型里面，最为高效的IO多路复用机制与设施。
<br />

## 五.克隆代码并启动运行 ##
<br />

### 1.从GitHub克隆版本库源码 ###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 打开控制台Console程序，执行mkdir RpcEngineering命令，新建RPC框架源码的工作空间目录；并执行cd命令进入该目录，然后执行git clone https://github.com/shaojianqing/RpcSystem.git命令，从GitHub版本库中克隆源码到工作空间目录中。完整的步骤如下图所示：
![image](https://github.com/shaojianqing/RpcSystem/blob/master/images/cloneSourceCode.png)
### 2.使用eclipse导入源码并生成maven工程 ###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 打开eclipse(建议较高版本的eclipse)，并将eclipse的工作空间设置为RpcEngineering目录。然后在eclipse中进行导入Existing Maven Projects操作，选择上一步中clone下来的rpc-system工程目录。最后完成工程导入操作，如下图所示：
![image](https://github.com/shaojianqing/RpcSystem/blob/master/images/importSourceCode.png)
### 3.安装Postgresql数据库并创建对应数据表 ###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 安装postgresql数据库系统，并设置postgresql账户的密码为postgres，最后登录数据库管理工具pgAdmin，并创建测试数据库System，以及其中的所有数据表。如下图所示：
![image](https://github.com/shaojianqing/RpcSystem/blob/master/images/rpcDatabase.png)

对于上述数据库和数据表，建议采用DDL语句进行创建，以下为建库和建表DDL语句:

```
-- Database: System

-- DROP DATABASE "System";

CREATE DATABASE "System"
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'zh_CN.UTF-8'
    LC_CTYPE = 'zh_CN.UTF-8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE "System"
    IS 'RPC.System的开发数据库';
    
    
    
-- Table: public.accountbill

-- DROP TABLE public.accountbill;

CREATE TABLE public.accountbill
(
    id character varying(32) COLLATE pg_catalog."default" NOT NULL,
    userinfoid character varying(32) COLLATE pg_catalog."default" NOT NULL,
    accountbillno character varying(32) COLLATE pg_catalog."default" NOT NULL,
    amount double precision NOT NULL,
    billtype character varying(40) COLLATE pg_catalog."default" NOT NULL,
    isvalid smallint NOT NULL,
    createtime time with time zone NOT NULL,
    operatetime time without time zone NOT NULL,
    billtime bigint NOT NULL
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.accountbill
    OWNER to postgres;
    
    

-- Table: public.accountinfo

-- DROP TABLE public.accountinfo;

CREATE TABLE public.accountinfo
(
    id character varying(32) COLLATE pg_catalog."default" NOT NULL,
    userinfoid character varying(32) COLLATE pg_catalog."default" NOT NULL,
    totalbalance double precision NOT NULL,
    totalexpanse double precision NOT NULL,
    totalprofit double precision NOT NULL,
    isvalid smallint NOT NULL,
    createtime time without time zone NOT NULL,
    operatetime time with time zone NOT NULL
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.accountinfo
    OWNER to postgres;
    
    
    
-- Table: public.operaterecord

-- DROP TABLE public.operaterecord;

CREATE TABLE public.operaterecord
(
    id character varying(32) COLLATE pg_catalog."default" NOT NULL,
    operatorid character varying(32) COLLATE pg_catalog."default" NOT NULL,
    operatetype character varying(40) COLLATE pg_catalog."default" NOT NULL,
    targettype character varying(40) COLLATE pg_catalog."default" NOT NULL,
    targetid character varying(32) COLLATE pg_catalog."default" NOT NULL,
    isvalid smallint NOT NULL,
    createtime time with time zone NOT NULL,
    operatetime time with time zone NOT NULL
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.operaterecord
    OWNER to postgres;
    
    
    
-- Table: public.sequence

-- DROP TABLE public.sequence;

CREATE TABLE public.sequence
(
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    value bigint NOT NULL,
    minvalue bigint NOT NULL,
    maxvalue bigint,
    createtime time without time zone NOT NULL,
    operatetime time with time zone NOT NULL
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.sequence
    OWNER to postgres;
    
   
    
-- Table: public.userinfo

-- DROP TABLE public.userinfo;

CREATE TABLE public.userinfo
(
    id character varying(32) COLLATE pg_catalog."default" NOT NULL,
    username character varying(40) COLLATE pg_catalog."default" NOT NULL,
    password character varying(40) COLLATE pg_catalog."default" NOT NULL,
    name character varying(40) COLLATE pg_catalog."default",
    birthday date NOT NULL,
    idnumber character varying(80) COLLATE pg_catalog."default" NOT NULL,
    isvalid smallint NOT NULL,
    createtime timestamp without time zone NOT NULL,
    operatetime timestamp without time zone NOT NULL,
    CONSTRAINT userinfo_pkey PRIMARY KEY (id),
    CONSTRAINT username_unique UNIQUE (username)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.userinfo
    OWNER to postgres;
    
```
### 4.在本地的hosts设置RPC运行过程中所涉及的域名与IP映射关系 ###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 由于在RPC调用过程中，涉及的各项管控与路由组件访问的地址均为域名形式。于此同时，真正进行各项管控与路由的机制需要基于IP地址进行直接的通信，因此需要在本地hosts文件中设置域名与IP的映射关系配置。具体配置内容请参考以下图示：
![image](https://github.com/shaojianqing/RpcSystem/blob/master/images/hostConfig.png)
### 5.启动程序生成所有测试数据 ###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 完成上述各项准备工作之后，运行环境已经准备完成，接下去要进行测试数据的准备工作。笔者已经在RPC框架中开发了测试数据生成的相关实现类，可以直接调用TestDataGenerator，并启动其中的main方法即可生成全部的测试数据。完成测试数据的生成工作之后，请登录数据库管理工具，在postgresql数据库中查看生成的测试数据是否正确以及符合预期。具体操作如下图所示：

![image](https://github.com/shaojianqing/RpcSystem/blob/master/images/generateTestData.png)

生成测试数据的程序TestDataGenerator

![image](https://github.com/shaojianqing/RpcSystem/blob/master/images/checkTestData.png)

检查测试程序生成的数据是否正确并符合预期

### 6.选择测试数据的userId值 ###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 测试数据生成完成之后，考虑到RPC框架按照userId的最后两位进行数据的分布与定位，以及通过userId进行各项数据和测试操作。因此，需要基于生成的测试数据，在服务消费者的rpc调用程序里面，根据userId的数据分布规则，进行userId选取和设置。具体操作如下图所示：
![image](https://github.com/shaojianqing/RpcSystem/blob/master/images/selectUserIdData.png)
### 7.启动各项服务组件和服务提供者应用 ###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 完成上述各项操作与准备工作之后，环境与数据的准备工作已经全部完成。之后需要逐步启动各项RPC管控与路由的基础服务设施，以下按顺序分别启动各项RPC基础服务:

* 启动Zone部署信息管理组件ZoneManager，具体应用为rpc.system.manager。
* 启动软负载配置中心管理组件Confreg，具体应用为rpc.system.confreg。
* 启动跨城接口访问代理组件Proxy，具体应用为rpc.system.proxy。
* 启动域名映射与应用状态管理组件Vip，具体应用为rpc.system.vip。
* 启动服务提供者实现组件，具体应用为rpc.system.provider。

### 8.启动服务消费者应用并查看服务调用日志 ###
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 最后，在完成上述各项准备工作之后，即可进行RPC的验证与测试过程。直接启动服务消费者实现组件，具体应用为rpc.system.consumer。之后，查看服务消费方的控制台输出和服务提供方的控制台输出，具体操作如下图所示：
![image](https://github.com/shaojianqing/RpcSystem/blob/master/images/startTestData.png)

启动并运行服务消费者测试程序

![image](https://github.com/shaojianqing/RpcSystem/blob/master/images/testOutputClient.png)

服务消费者测试日志输出

![image](https://github.com/shaojianqing/RpcSystem/blob/master/images/testOutputServer.png)

服务提供者测试日志输出

通过上述各项RPC组件和设施的日志输出，即可分析整个RPC过程内部机制与原理等。
<br />
<br />
<br />