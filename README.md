# uaiFome
projeto final para avaliação semestral.

# Firebase dependencias
# a nível de aplicação
dependencies { ...
    implementation 'com.google.firebase:firebase-core:15.0.0'
    
    implementation 'com.google.firebase:firebase-storage:15.0.0'
    
    implementation 'com.google.firebase:firebase-auth:15.0.0'
    
    implementation 'com.google.firebase:firebase-database:15.0.0'
    }
  apply plugin: 'com.google.gms.google-services'
# a nível de projeto
    dependencies {
        ...
        classpath 'com.google.gms:google-services:4.2.0' 
        }
