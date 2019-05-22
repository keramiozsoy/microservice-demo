# microservice-demo application

# config server repo 

Projemizdeki dev,prod ortamlarına özel properties dosyaları içeren depodur.
    
  - git clone https://github.com/keramiozsoy/microservice-demo-config-server-repo.git
  - cd microservice-demo-config-server-repo
  - git reset --hard 90b03f93c6017a4da5434d34bd5a264f72ff9c0c

# config server

Diğer microservislerin tüm config bilgilerini alacağı projedir. 

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

 config server repo projesinde ayarlarda değişiklikler olduğu zaman config server projesini restart etmeksizin son config bilgilerini almamızı sağlayan projedir.
 

config server projesinde dev, test , prod ortamlarına göre properties dosyalarını okumak için şu istekleri yapabiliriz.

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
  
  
