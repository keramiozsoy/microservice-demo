# microservice-demo application

Java dilinde basit mikroservisler ile çalışan mimari geliştirelim.

# microservice-demo-config-server-repo 

Projemizdeki development, production ortamlarına özel properties dosyaları içeren depodur.
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

Client projesi   ` application ` veya ` bootstrap `  isimli dosyada konfigürasyonun nereden alınacağı yani
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
 
 
 `service-discovery` projesi desteklemek isteği özellikler ile çalışabilmesi için yani bir kayıt defteri gibi çalışması için ana sınıfı 
 `
 @EnableEurekaServer
 `
 ile işaretlenmelidir.

Kayıt olacak olan projelerin kendilerini çalıştıran ana sınıf `@EnableEurekaClient` veya  `@EnableDiscoveryClient`
ile işaretlenmelidir.

`@EnableDiscoveryClient`  
 Eureka, ( https://netflix.github.io )
 Consul  (  consul.io ), 
 Zookeeper ( zookeeper.apache.org )  desteklerken , `@EnableEurekaClient` sadece  Netflix tarafından geliştirilen  Eureka yı desteklemektedir. Bu projede Eureka kullanılmaktadır.



