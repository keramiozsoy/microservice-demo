# microservice-demo application

Java dilinde basit mikroservisler ile çalışan mimari geliştirelim.

# microservice-demo-config-server-repo 

Projemizdeki development, production ortamlarına özel properties dosyaları içeren depodur.
```
    git clone https://github.com/keramiozsoy/microservice-demo-config-server-repo.git
    
    cd microservice-demo-config-server-repo
    
    git reset --hard 2dfc9d8d293aa86e0781ac7a328fe4c5f27e532a
```
# config-server

Microservislerin tüm konfigürasyon bilgilerini aldığı projedir. 
Projeye 8000 protundan erişilmektedir.

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
        

`curl` yardımı ile `client` projesine özel `client-config` isimli konfigürasyon dosyasına ulaşabiliyoruz.
Fakat projenin çalışırken kendisinin ulaşabilmesi için şu maddeleri uygulamalıyız.



Client projesi  
` application ` veya ` bootstrap `  isimli dosyada konfigürasyonu alacağı  projenin `config-server` istek atılacak uri belirtmelidir.  


` spring.cloud.config.uri=http://localhost:8000 ` 
ayrıca client projesi hangi konfigürasyonları alacak ise bunu da aşağıdaki gibi properties dosya isi ile belirtmeliyiz.
` spring.application.name=client-config ` 
    

Bunun için ` actuator refresh endpoint ` üzerinden POST isteği atarak son konfigürasyon bilgilerin alabilirsiniz.
Buradaki güzel durum projenin kapamanmadan son bilgileri alabilmesidir.

```

    curl  -X POST http://localhost:8080/actuator/refresh -d '{}' -H "Content-Type: application/json"

```

Denemek için `microservice-demo-config-server-repo` projesinde değişiklik yapıp `refresh` isteği yapabilir.

veya

Projeler çalışırken daha önceden bilgisayarınıza çekilmiş profillere ait konfigürasyon dosyalarında değişiklikler yaparsanız asıl proje bu değişiklikleri içermeyeceği için `refresh` isteği yapıldığında değiştirilmemiş halini tekrardan
çekeceği için farkı kavrayabiliriz.

