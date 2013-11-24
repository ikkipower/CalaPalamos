# CalaPalamos


## LaFoscaMain

Activity principal, que implementa la funcionalidad del LogIn. Contiene un link para ir a registrarse como usario de la aplicación. 
En caso de que el login sea correcto, la aplicación pasará a la activity OptionActivity donde tendremos a nuestra disposición las funcionalidades implementadas. En caso contrario, se mostrará Alertdialog de error.

## RegisterActivity

Activity que coge los datos del nuevo usuario (username y passwd) y los devuelve a la activity LaFoscaMain donde se procede al registro.

## OptionsActivity

Activity que contiene tres tabs (State, Weather, Change), que almacenan todas las funcionalidades de la aplicación

### State

Este fragment contiene todas las funcionalidades que nos muestra los datos del estado de la playa así como también implementa un botón que llama a la activity (KidsListActivity).

#### KidsListActivity

Activity que implementa una navigation bar con un searchview y un listview. Nos muestra la lista de niños que hay en la playa ordenados por edad de mayor a menor.

### Weather

Fragment que muestra los datos del tiempo en Palamós mediante la Api de Openweathermaps. Esta api coge los datos de diversas estaciones meteorológicas, a pesar que se sitúa por coordenadas. Cómo no siempre coge los datos de la misma estación a veces puede aparecer el nombre de poblaciones cercanas a Palamós cómo Calonge, por poner un ejemplo. Esto es debido a como fúnciona la api, no es debido al funcionamiento de la app.

### Change

Fragment que implementa las diferentes opciones para cambiar el estado de la playa (abrir/cerrar playa, cambiar bandera, lanzar balones nivea o limpiar). El resultado del cambio del estado de la playa afecta directamente a como se muestran los datos, ya que si cerramos la playa se activan/desactivan las diferentes opciones disponibles si la playa está cerrada.
