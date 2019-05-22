# microservice-demo application

# config server repo 
    Projemizdeki dev,test,prod ortamlarına özel properties dosyaları içeren depodur.
    
  - git clone https://github.com/keramiozsoy/microservice-demo-config-server-repo.git
  - cd microservice-demo-config-server-repo
  - git reset --hard 90b03f93c6017a4da5434d34bd5a264f72ff9c0c

# config server
    Diğer microservislerin tüm config bilgilerini alacağı projedir. 
  
 - gradle 5.4.1 
 - java 1.8
 - springboot 2.2 (SNAPSHOT) 
 - group : com.microservice.demo
 - artifact : configserver
 - name : configserver
 - dependencies : Config Server
 
     config server, config server repo projesinde ayarlarda değişiklikler olduğu zaman config server projesini restart etmeksizin son config bilgilerini almamızı sağlayan projedir.
  
  
  
  
