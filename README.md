# IFT1025-TP2-server

Anne Sophie Rozéfort - 20189221

lien Vidéo(Sharepoint) : https://udemontreal-my.sharepoint.com/:v:/r/personal/anne_sophie_rozefort_umontreal_ca/Documents/Rozefort-TFT1025-TP2-Video.mp4?csf=1&web=1&e=rY8Jav

lien Video(DropBox) : https://www.dropbox.com/s/foq1jet4jbv093q/Rozefort-TFT1025-TP2-Video.mp4?dl=0

Path des jars :
Server - /out/artifacts/"server.jar"
Client_simple - /out/artifacts/"Client_simple.jar"
Client_Fx - /out/artifacts/"Client_fx.jar"

Assurez vous que dans les File-Project Structures - dependencies de ClientFx, le module server (je ne sais pas pourquoi lorsque je compresse le fichier pour le partager,le module Server n'est plus)

Je sais qu'il etait possible de faire Client_simple et Client_Fx dans un seul module, cependant j'ai du les séparés à cause de cette erreurs constantes (même après avoir ajouté vm options, javaFx librairies,etc):

Graphics Device initialization failed for :  es2, sw
Error initializing QuantumRenderer: no suitable pipeline found

La conséquence, je n'ai pas pu import sever.models.Course et server.models.RegistrationForm dans Client_fx, donc le dernier bout de l'inscription ne marche pas.
