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
 - dependencies : Config Client , Actuator , Web , Rest Repositories
 
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
 - name : configserver
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
 
 
**client2** isimli bir uygulama *client* isimli uygulamaya erişmek için istek atsın. 
**service-discovery** bu iki uygulamanin yerlerini ve portlarini bilmektedir.

**client2** uygulaması **client** uygulamasının ulaşım bilgilerini **service-discovery** den 
aldıktan sonra  **netflix-ribbon** kütüphanesi yardımıyla bilgileri kendisine **cache** mekanizmasına kaydeder.
Bu kaydetme işlemini uygulama periyodik olarak yaparak bilgilerin son halini tutmayı hedefler.
 
**netflix-ribbon** daha sonraki aşamalarda ayrıntılı açıklanacaktır.
  

# client2 

**client** projesinin bir kopyasi olup bazı yardımcı fonksiyonlar eklenmiştir.

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






