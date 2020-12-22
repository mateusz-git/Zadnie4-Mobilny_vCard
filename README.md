# Dokumentacja

## Zadnie4-Mobilny_vCard

### Endpoint
Metoda : GET

Ścieżka : /searchProfession
           
Opis :  Strona wyszukiwania zawodu z możliwością wpisania nazwy, po kliknieciu search przenosi nas to strony z listą wszystkich ludzi o danym zawodzie

_________________________________________________________________________________________

Metoda : GET

Ścieżka : /search?k={k}

Parametr : k(typ String) - nazwa zawodu
           
Opis :  Strona z lista ludzi o danym zawodzie "k" wraz z przyciskiem do pobierania vCard

_________________________________________________________________________________________

Metoda : GET

Ścieżka : /getvCard/{k}/{email}
     
Parametr : k(typ String) - nazwa zawodu

Parametr : email(typ String) - email osoby ze strony o danym zawodzie
      
Opis :  Pobiera plik z rozszerzeniem .vcf osoby o danym emailu oraz strony z danym zawodu 

## Przykłady użycia
Po wejsciu na strone :
``
http://localhost:8080/searchProfession
``
ukazuje się okno do wpisaia nazwy zawodu. Wpisujemy zawod a nastepnie klikamy search, np. malarz

Strona przenosi nas pod link 
``
http://localhost:8080/search?k=malarz
``
Strona ta wyświetla liste wszystkich ludzi o danym zawdzonie. Widzimy podstawowe dane osoby z przypisanym  przyciskiem o nazwie ,,Get vCard". Po kliknieciu w przycisk pobiera sie plik o nazwie email danej osoby z rozszerzeniem .vcf.
 