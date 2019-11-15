# microservice-demo application

Java dilinde basit mikroservisler ile çalışan mimari geliştirmeye çalışalım..

# microservice-demo-config-server-repo 

Projemizdeki development, production ortamlarına özel konfigürasyon(properties) dosyaları içeren depodur.
```
    git clone https://github.com/keramiozsoy/microservice-demo-config-server-repo.git
    
    cd microservice-demo-config-server-repo
    
    git reset --hard 3c23090b5c894cedb2286a6312046c21e75122e6
```
# config-server

Microservislerin tüm konfigürasyon bilgilerini aldığı projedir. 
Projeye 8000 portundan erişilmektedir.

```

    https://start.spring.io
    
 - gradle 5.4.1 
 - java 1.8
 - springboot 2.2 (SNAPSHOT) 
 - group : com.microservice.demo
 - artifact : configserver
 - name : configserver
 - dependencies : Config Server
 
```

 `microservice-demo-config-server-repo` projesinde tuttuğumuz bilgilere ulaşmamızı sağlayan proje `config-server` dır.
 
 Tüm projeler `config-server` projesine istekte bulunarak son konfigürasyonlar ile çalışırlar.
 
 
 Projemizin son konfigürasyonlara ulaşmasını ve bu rolü üstlenmesini sağlamak için, 
  
 -  ` @EnableConfigServer ` ile projeyi çalıştıran ana sınıf işaretlenmelidir.
 
 
 -  ` application ` veya ` bootstrap `  isimli dosyada konfigürasyonu alacağı projenin bilgisi şu şekilde eklemelidir.
    `  spring.cloud.config.server.git.uri=https://github.com/keramiozsoy/microservice-demo-config-server-repo  `
    
    ve hangi klasörlerde arama yapılabileceği, ulaşılabileceğini aşağıdaki şekilde belirtiyoruz.
    
    ` spring.cloud.config.server.git.searchPaths = client-project-configs ` 
    

Istenilen profillere göre konfigürasyonlara ulaşabildiğimizi kontrol edelim.

` / proje / port / properties dosyasının adı / profil  `

şeklindeki hiyerarşi ile isteğimizi gerçekleştirelim. Burada hangi klasöre gideceğimiz daha önceden belirtildiğinden
tekrar belirmemize gerek kalmadan doğrudan dosyalara ulaşabiliyoruz.

( config-server-development.properties , config-server-production.properties )

```

  curl http://localhost:8000/client-config/development

  curl http://localhost:8000/client-config/production

```

# client

İş mantığı geliştirdiğimiz projedir. 
Projeye 8001 portundan erişilmektedir.

```

    https://start.spring.io
    
 - gradle 5.4.1 
 - java 1.8
 - springboot 2.2 (SNAPSHOT) 
 - group : com.microservice.demo
 - artifact : client
 - name : client
 - dependencies : Config Client , Actuator , Web , Rest Repositories, Eureka Discovery Client
 
```

Proje seçilen bir profile göre development,production konfigürasyonlarını alarak çalışır.

`config-server` projesi, 
 `microservice-demo-config-server-repo` projesinde konfigürasyon değişikliği olduğunda 
 `config-server` ve `client` restart edilmeksizin `client` gibi konfigürasyonlar ile çalışan projelere iletebilir.
        

`curl` yardımı ile `client` projesine özel `client-config` isimli konfigürasyon dosyalarına `config-server`
projesine istekte bulunarak ulaşabiliyoruz.

Fakat `client` projesinin çalışırken kendi kendine ulaşabilmesi için şu maddeleri uygulamalıyız.

`client` projesi   ` application ` veya ` bootstrap `  isimli dosyada konfigürasyonun nereden alınacağı yani
 `config-server` projesininin uri bilgisi belirtilmeldir.  
``` 

    spring.cloud.config.uri=http://localhost:8000 
    
```
Client projesi hangi konfigürasyonları alacak ise bunu da aşağıdaki gibi properties dosya ismi ile belirtilmelidir.
```

    spring.application.name=client-config
    
```


`client` ve `config-server`  projesini çalıştırıp `client` projesine istek atarak konfigürasyonlar içindeki bir değere nasıl ulaşabileceğimizi görelim.

```

    curl http://localhost:8001/message 
    
```
Bilgilere ulaşabiliyoruz.


`microservice-demo-config-server-repo` projesinde bir değişiklik yapıldığında `message` isteğinin
sonuçlarını kontrol etmek isteyebiliriz.

Bunun için ` actuator refresh endpoint ` üzerinden POST isteği atarak son konfigürasyon bilgilerin alabilirsiniz.
Tabi bu metodun `@RefreshScope` ile işaretlendiğini unutmayalım.
Buradaki güzel durum projenin kapamanmadan son bilgileri alabilmesidir.

```

    curl  -X POST http://localhost:8001/actuator/refresh -d '{}' -H "Content-Type: application/json"

```

Denemek için `microservice-demo-config-server-repo` projesinde değişiklik yapıp `refresh` isteği yapabilir.

veya

Projeler çalışırken daha önceden bilgisayarınıza çekilmiş profillere ait konfigürasyon dosyalarında değişiklikler yaparsanız asıl proje bu değişiklikleri içermeyeceği için `refresh` isteği yapıldığında değiştirilmemiş halini tekrardan
çekeceği için farkı kavrayabiliriz.



# service-discovery
Tüm ulaşılabilir projelerin bilgilerinin tutulduğu projedir.
Projeye 8761 portundan erişilmektedir.

```

    https://start.spring.io
    
 - gradle 5.4.1 
 - java 1.8
 - springboot 2.2 (SNAPSHOT) 
 - group : com.microservice.demo
 - artifact : servicediscovery
 - name : servicediscovery
 - dependencies :  Eureka Server, Actuator
 
```

 Erişilmek isteyen  `client` gibi iş mantığı geliştirdiğimiz projeler `service-discovery` projesine kendilerinin bilgilerini kayıt ettirirler. `service-discovery` burada telefon defteri gibi düşünebiliriz. Tüm projelerin bilgisi burada vardır.
 
`service-discovery` projesinin ` application ` veya ` bootstrap `  isimli  konfigürasyon dosyasında
aşağıdaki satırları, kendi kendine kayıt olmaya çalışmasını engellemek ve  client olarak çalışırken alması gereken kayıt defteri(eureka) bilgilerini almaması için eklemeliyiz.
 
```

##  Ayni sistemde bir çok **service-discovery** olabilmektedir. 
## Kendini başka **service-discovery** projesine kayıt yapmasini
## engellemek için aşağıdaki ayarlar verilir

eureka.client.registerWithEureka=false
eureka.client.fetchRegistry=false

```
 
 
 `service-discovery` projesi desteklemek isteği özellikler ile çalışabilmesi için yani bir kayıt defteri gibi çalışması için ana sınıfı 
 `
 @EnableEurekaServer
 `
 ile işaretlenmelidir.

Kayıt olacak olan projelerin kendilerini çalıştıran ana sınıf `@EnableEurekaClient` veya  `@EnableDiscoveryClient`
ile işaretlenmelidir.

`@EnableDiscoveryClient` ile işaretlenirse

     Eureka, ( https://netflix.github.io )
     Consul  (  https://www.consul.io ), 
     Zookeeper ( https://zookeeper.apache.org )  
 
desteklemek için genel bir alt yapı sağlanırken

 `@EnableEurekaClient` sadece Eureka yı desteklemektedir. Bu projede Eureka kullanılmaktadır.
 
 `client` uygulamasına `@EnableEurekaClient` ekleyip `application` veya `bootstrap` isimli konfigürasyon dosyasında
 `service-discovery` url bilgisini eklemeliyiz.
 
```

    eureka.client.serviceUrl.defaultZone= http://127.0.0.1:8761/eureka/
    eureka.client.healthcheck.enabled=true
    eureka.client.lease.duration=5
    
    ## Kendini **service-discovery** projesine kayit et
    eureka.client.registerWithEureka=true
    
    ## **service-discovery** projesinden ulaştığı bilgilerin kendisine kopyasini *cache* olarak almasini sağlar 
    eureka.client.fetchRegistry=true


    ## Eureka bu proje çalistiginda kaç sn sonra kendini kayit ettirecegi varsayilan 30sn
    eureka.instance.leaseRenewalIntervalInSeconds=1
    
    ## 
    eureka.instance.leaseExpirationDurationInSeconds=2
    
    ##    Eureka ya servisler  kayıt olurken varsayılan olarak proje isimleri(hostname)
    #  ile kayıt olurlar. Kendi bilgisayarımız, sunucu ortamlarında bu isimler 
    #  ile çalışılabilir. Fakat konteyner bazlı deploy  yapılmış uygulamalarınızı düşünürsek,
    #  her konteyner oluşturulurken rastgele bir isim ile oluşturulur.
    #  Konteynerda dns girdisi olmadığı durumda, uygulamalarımızın isimleri(hostname) 
    # doğru şekilde  çözümlenemeyecektir. Bu nedenle her zaman true olmalıdır.
    #
    #  Cloud Native uygulamalar, temel destekledikleri durumlar, kısa ömürlü (ephemerel) ve 
    #   sunucunun istemciye ait bir bilgisini  tutmadığı (stateless) uygulamalardır.
    #   Bu durumları desteklemek için true olmalıdır. :)
    eureka.instance.preferIpAddress=true
    
    
```

 Tüm uygulamaların toplu halde bilgilerini almak istersek şu url bilgisini
 kullanabiliriz. Varsayılan olarak xml formatında veri sağlanmaktadır.
 Fakat **header** json olarak istek atılarak json veri formatında dönüş sağlanabilir.
 
 ```
 http://localhost:8761/eureka/apps
 
 ```

**client** uygulamamiz **service-discovery** uygulamasina artık adını yazdirabilir durumdadır.
Ayrıca **client** kendinden farklı uygulamalardan istekleri alip cevaplayabilecek durumdadir. 
 
 
**client** uygulamasi **client site load balancing** yapabilmesi için ayarların yapılması gerekmektedir. 
 
**netflix-ribbon** kütüphanesi kullanarak bu durum kolayca gerçekleştirilebilir. 
Peki neden böyle bir şeye ihtiyaç var?
 
 
# client2
3 farklı türdeki web servis isteği atma yöntemini örneklediğimiz uygulamadır.
Projeye 8002 portundan erişilmektedir.

```

    https://start.spring.io
    
 - gradle 5.4.1 
 - java 1.8
 - springboot 2.2 (SNAPSHOT) 
 - group : com.microservice.demo
 - artifact : client
 - name : client
 - dependencies :  Eureka Discovery Client, Actuator, Web, openfeign
``` 

**client** projesinin bir kopyasi olup bazı yardımcı fonksiyonlar eklenmiştir.

**client2** isimli bir uygulama **client** isimli uygulamaya erişmek için istek atsın. 
**service-discovery** bu iki uygulamanin yerlerini ve portlarini bilmektedir.

**client2** uygulaması **client** uygulamasının ulaşım bilgilerini **service-discovery** den 
aldıktan sonra  **netflix-ribbon** kütüphanesi yardımıyla bilgileri kendisine **cache** mekanizmasına kaydeder.
Bu kaydetme işlemini uygulama periyodik olarak yaparak bilgilerin son halini tutmayı hedefler.
 
**netflix-ribbon** daha sonraki aşamalarda ayrıntılı açıklanacaktır.
  

 ## Spring kullanırken projeden projeye rest isteklerini nasil atabiliriz ?
 
 Bu durumu gerçekleştirebileceğimiz 3 farklı yol mevcuttur.
 - Spring Discovery kullanarak
 - Spring Discovery ve RestTemplate beraber kullanarak
 - Netflix Fiegn kullanarak
 
 Her durumu örnekler ile açıklamak için **client** ve  **client2** projelerindeki geliştirmelere bakalım.
 
 ### Spring Discovery kullanarak
 
 Kendisini **service-discovery**  projesinde kayıt yaptıran tüm projeler arasından ulaşmak istediğimize 
 **service-discovery** üzerinden proje adı ile erişerek istek atılmasıdır.
 
 ```
 https://github.com/keramiozsoy/microservice-demo/blob/master/client2/src/main/java/com/microservice/demo/client2/restTypes/TypeOneWithDiscoveryClient.java
 
 ```
 
 ### Spring Discovery ve RestTemplate beraber kullanarak
 
 Projeden projeye ad,ip ve port gibi bilgiler yardımıyla istek atılmasını sağlar.
 ```
 https://github.com/keramiozsoy/microservice-demo/blob/master/client2/src/main/java/com/microservice/demo/client2/restTypes/TypeTwoRestTemplateClient.java
 
 ```
 
 ### Netflix Fiegn kullanarak
 
 Java daki **interface** tanımlaması yapılarak projenin ismi yazılarak **service-discovery** üzerinden
 port bilgisine otomatik ulaşılarak isteklerin atılmasını sağlar.
 
 Projemizi çalıştırdığımız sınıfa **@EnableFeignClients** eklemeliyiz.
 Bu yöntemin kullanıldığı her **interface** **@FeignClient** ile işaretlenmelidir.
 
 ```
 https://github.com/keramiozsoy/microservice-demo/blob/master/client2/src/main/java/com/microservice/demo/client2/restTypes/TypeThreeFeignClient.java
 ```
 
 Örnek isteklerimizi yapalım.

**client2** uygulamasıdaki **CallInfoController** sınıfına istek atarak,

**client** uygulamasındaki **InfoController** sınıfına erişebileceğimizi görebiliriz.
 
 
 ```
curl --request GET http://localhost:8002/call/1
curl --request GET http://localhost:8002/call/2
curl --request GET http://localhost:8002/call/3
 
 ```
 
 Yukarıdaki yöntemlerden sadece birini seçerek tüm projenizdeki isteklerin çalışmasını sağlayabilirsiniz.
 

# Netflix Hystrix

    https://github.com/Netflix/Hystrix
    
    (Bu konu anlatılacak)

# authorization-server

Tüm ulaşılabilir projelerin bilgilerini diğer projelere güvenli şekilde açmak için 
token, OAuth2 gibi yapıların kullanılmasını sağlayan projedir.
Projeye 8100 portundan erişilmektedir.

```

    https://start.spring.io
    
 - gradle 5.4.1 
 - java 1.8
 - springboot 2.2 (SNAPSHOT) 
 - group : com.microservice.demo
 - artifact : authorizationserver
 - name : authorizationserver
 - dependencies :  Web, Security, Cloud OAuth2, Eureka Discovery Client
 
```
**@EnableAuthorizationServer** ile token, şifreleme, kullanici adi ve şifre kontrollerinin yapıldığı 
sınıfı işaretlemiş olduk. (bu kısmı tekrar araştıracağım bunlar farklı sınıf imlementasyonları ile yapılabilir mi ?)

**@EnableWebSecurity** ile kimlik doğrulama (**Authentication**) alt yapısı yani kullanıcı adi ve şifre kontrol mekanizmasının  yazıldığı  sınıfı işaretlemiş olduk. Hangi kullanıcı hangi rolde bilgisini sağlamış olduk.

Ayrıca bu sınıf bize kimliği doğru olan kullanıcının hangi url bilgilerine erişme (**Authorization**) yetkisine sahip olduğunu tanımalaycak alt yapı sunmaktadır.   (bu kısmı tekrar araştıracağım bunlar farklı sınıf imlementasyonları ile yapılabilir mi ?)





Doğru kullanıcılar ile **HTTP Auth** veya **Basic Auth** denilen yöntem ile deneme yapabiliriz. 
Sadece url bilgisini tarayıcı üzerinden çağırarakta bu testi gerçekleştirebiliriz.
```
curl -X GET -u user:secret http://localhost:8100/actuator/
curl -X GET -u admin:secret http://localhost:8100/actuator/
```

Yukarıda basitçe kimlik doğrulama mekanizması çalışıyormu kontrol ettik.

Token mekanizmamız çalışmıyor mu kontrol edelim.

```
POSTMAN

POST http://localhost:8100/oauth/token

Authorization Tab
Basic Auth 
Username      : client
Password      : password

Body Tab

username:admin
password:secret
grant_type:password

İstek yap sonucu gör :)

{
    "access_token": "c57fcf48-1c8a-4206-9aaa-4a9a0e8dea2d",
    "token_type": "bearer",
    "refresh_token": "e6b87ec4-859f-4876-9adb-12335508effa",
    "expires_in": 43052,
    "scope": "read write"
}

```

Alınan token bilgisinin kontrolü için isteklerimizi hazırlayalım.

```
POSTMAN

POST http://localhost:8100/oauth/check_token

Authorization Tab
Basic Auth 
Username      : client
Password      : password

Body Tab 
token:c57fcf48-1c8a-4206-9aaa-4a9a0e8dea2d

İstek yap sonucu gör :)

{
    "active": true,
    "exp": 1573720277,
    "user_name": "admin",
    "authorities": [
        "ROLE_ADMIN"
    ],
    "client_id": "client",
    "scope": [
        "read",
        "write"
    ]
}
```

Artık herhangi bir uygulamamızı token mekanizması yardımıyla çalışabilmesi için tasarlayabiliriz.


# client3

Profil bilgilerine göre erişilebilecek url bilgilerinin alt yapısını sağlayan uygulamadır.
Projeye 8003 portundan erişilmektedir.

```

    https://start.spring.io
    
 - gradle 5.4.1 
 - java 1.8
 - springboot 2.2 (SNAPSHOT) 
 - group : com.microservice.demo
 - artifact : client
 - name : client
 - dependencies :  Eureka Discovery Client, Actuator, Web,  Security, Cloud OAuth2
 
```

Aşağıdaki url bilgisinde herhangi bir kısıtlama yoktur.

```
curl -X GET http://localhost:8003/public
```

Aşağıdaki url bilgisine ulaşmak için o kullanıcıya ait **token** ile istek yapılmalıdır.

```
curl -X GET http://localhost:8003/admin  -H 'Authorization: Bearer b9e5985f-dce0-410d-ac29-fdbd02fa4653'
```
veya

```
POSTMAN

POST http://localhost:8003/admin

Headers Tab

Authorization:Bearer b9e5985f-dce0-410d-ac29-fdbd02fa4653

İstek yap sonucu gör :)
````


