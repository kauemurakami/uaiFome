# uaiFome
projeto final para avaliação semestral.
#Circle ImageView

        implementation 'de.hdodenhof:circleimageview:3.0.1'
        https://github.com/hdodenhof/CircleImageView
# Material Search View

    	implementation 'com.miguelcatalan:materialsearchview:1.4.0'
        https://github.com/MiguelCatalan/MaterialSearchView
        
# Firebase dependencias

        https://firebase.google.com/docs/?authuser=0
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
