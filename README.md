# microservice-demo application

Java dilinde basit bir mikroservis uygulaması geliştirelim.

# microservice-demo-config-server-repo 

Projemizdeki development, production ortamlarına özel properties dosyaları içeren depodur.
    
  - git clone https://github.com/keramiozsoy/microservice-demo-config-server-repo.git
  - cd microservice-demo-config-server-repo
  - git reset --hard 90b03f93c6017a4da5434d34bd5a264f72ff9c0c

# config-server

Microservislerin tüm konfigürasyon bilgilerini aldığı projedir. 

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
 
 
 `config-server` projesi, 
 `microservice-demo-config-server-repo` projesinde konfigürasyon değişikliği olduğunda 
 `config-server` restart edilmeksizin `client` gibi konfigürasyonlar ile çalışan projelere iletebilir.
 

config server projesinde dev, test , prod ortamlarına göre properties dosyalarını okumak için çalıştırıp şu istekleri yapabiliriz.

properties dosyalarının ismi ile url üzerinden çağrıyoruz.

```
- curl http://localhost:8888/config-server/development
- curl http://localhost:8888/config-server/production
```

properties dosyalarını okuyabilmek için url bootstrap isimli property dosyasına yazılmasını sebebi, 
bootstrap proje çalıştırıldığında ilk çalışacak olan property dosyasıdır.

```
bootstrap.properties  
    spring.cloud.config.server.git.uri  // bu property okumayı sağlıyor.
```

```
 - curl http://localhost:8080/actuator/health
 ```
 
# client

İş mantığı geliştirdiğimiz projedir. 

Proje seçilen bir profile göre çalışır  ( development,production )

config server repo projesinde yani tüm projeyi etkileyen, 
ayar dosyalarının bulunduğu projede değişiklikler olduğunda son değişiklikleri 
asıl projeler değişmeden alabilmeliyiz. 

Bunun için actuator yardımı ile refresh endpoint üzerinden POST isteği atarak son config bilgilerin alabilirsiniz.
Buradaki güzel durum projenin kapamanmadan son bilgileri alabilmesidir.

```
curl  -X POST http://localhost:8080/actuator/refresh -d '{}' -H "Content-Type: application/json"
```

