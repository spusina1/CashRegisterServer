# Feature
Dobavljanje stanja za poslovnicu
## Item
Korisnik Cash Register i Seller App treba moći pristupit svim neophodnim podacima koje Cash Register server dobiva od Main servera.
### Task
* Kreirati User model
* Kreirati Product model
* Kreirati Branch model
* Kreirati CashRegister model
* Kreirati Role model
* Kreirati RoleName model
* Implementirati metodu za dobavljanje podataka od Main servera
* Uraditi push koda
* Napraviti pull request

# Feature
Login korisnika
## Item
Korisnik Cash Register desktop aplikacije i Seller mobilna aplikacije treba vršiti prijavu korisničkog računa preko Cash Register servera, koji sve pristupne podatke o uposlenicima poslovnice/a dobavlja od main servera, zbog provjere podataka prije odobravanja pristupa kako bi se osigurala sigurnost veze. 
* Implementirati LoginRequest
* Implementirati LoginResponse
* Implementirati UserController
* Implementirati UserService
* Implementirati UserRepository
* Sigurnost i konfiguracja
* Uraditi push koda
* Napraviti pull request

# Feature
Pregled podataka o svim proizvodima
## Item
Korisnik Cash Register i Seller App treba imati pregled informacija o svim proizvodima, preko Cash Register servera.
### Task
* Implementirati ProductController
* Implementirati ProductService
* Implementirati ProductRepository
* Implementirati metode za vraćanje informacija o svim proizvodima iz baze
* Uraditi push koda
* Napraviti pull request

# Feature
Filtrirani pregled podataka o svim proizvodima
## Item
Korisnik Cash Register desktop aplikacije i Seller mobilne aplikacije treba biti u mogućnosti filtrirati spisak svih artikala u poslovnici, o kojima informacije čuva Cash Register server, kako bi brže i jednostavnije dolazio do podataka o trenutno potrebnim artiklima. 
### Task
* Implementirati FilterRequest klasu
* Implementirati metodu unutar ProductService koja na osnovu filterRequest vraća listu proizvoda
* Implementirati metodu Product Controllera koja vraća filtrirane proizvode 
* Uraditi push koda
* Napraviti pull request

# Feature
Spašavanje narudžbi koje je kreirala Seller App
## Item
Korisnik Seller mobilne aplikacije treba biti u mogućnosti spasiti narudžbu.
### Task
* Implementirati Receipt model
* Implementirati Receipt service
* Implementirati Receipt repository
* Implementirati POST metodu za spremanje narudžbe
* Implementirati spašavanje narudžbe u formi računa
* Vratiti odgovor klijentu
* Uraditi push koda
* Napraviti pull request

# Feature
Pregled svih računa koje je kreirala Seller App
## Item
Korisnik Cash Register aplikacije treba biti u mogućnosti pregledati sve račune koje je kreirala Seller App.
### Task
* Implementirati GET metodu za dobavljanje računa
* Provjera da li je račun kreirala mobilna app
* Implementirati dobavljanje računa
* Vratiti odgovor klijentu
* Uraditi push koda
* Napraviti pull request

# Feature
Pregled svih računa
## Item
Korisnik Cash Register aplikacije treba biti u mogućnosti pregledati sve račune.
### Task
* Implementirati GET metodu za dobavljanje računa
* Implementirati dobavljanje računa
* Vratiti odgovor klijentu
* Uraditi push koda
* Napraviti pull request

# Feature
Spašavanje računa 
## Item
Korisnik Cash Register aplikacije treba biti u mogućnosti spasiti račun.
### Task
* Implementirati POST metodu za spremanje računa
* Provjera da li je račun kreirala mobilna app
* Implementirati provjeru da li je račun već spašen
* Implementirati spašavanje računa
* Vratiti odgovor klijentu
* Uraditi push koda
* Napraviti pull request

# Feature
Brisanje računa
## Item
Korisnik Cash Register aplikacije treba biti u mogućnosti obrisati račun koji neće biti plaćen.
### Task
* Implementirati DELETE metodu za brisanje računa
* Provjera da li je račun već obrisan
* Implementirati brisanje računa
* Vratiti odgovor klijentu
* Uraditi push koda
* Napraviti pull request

# Feature
Plaćanje računa
## Item
Korisnik treba biti u mogućnosti platiti račun pomoću PayApp.
### Task
* Primanje računa od Cash Register App koji se treba platiti.
* Provjera vrste plaćanja
* Slanej računa Main serveru
* Provjere odgovora od Main servera
* Slanje odgovora aplikaciji da li je račun plaćen
* Spašavanje računa
* Uraditi push koda
* Napraviti pull request

# Feature
Pregled dnevnog izvjštaja o transakcijama.
## Item
Korisnik Cash Register App treba imati mogućnost pregledati sve račune koji su tog dana izdati kroz taj Cash Register.
### Task
* Kreirati end point
* Implementirati metodu za vraćanje svih računa izdatih zadnja 24h
* Uraditi push koda
* Napraviti pull request

# Feature
Spremanje notifikacija
## Item
Guest korisnik Seller App treba imati mogućnost poslati poruku konobaru.
### Task
* Implementirati Notification model
* Implementirati Notification controller
* Implementirati Notification service
* Implementirati Notification Repository
* Kreirati end point
* Implementirati metodu za spremanje svih poslatih poruka od strane Seller App.
* Uraditi push koda
* Napraviti pull request


# Feature
Slanje notifikacija
## Item
Seller App i Cash Register App trebaj imati mogućnost pregledati poruke od Guest usera.
### Task
* Kreirati end point
* Implementirati metodu za slanje svih poruka koje do tada nisu poslane nekoj od App koja te poruke treba primiti.
* Uraditi push koda
* Napraviti pull request

# Feature
Uređivanje narudžbe 
## Item
Seller App i Cash Register App trebaj imati mogućnost uređivati narudzbe koje nisu plaćene.
### Task
* Kreirati end point
* Implementirati metodu koja provjerava da li je račun plaćen, te ukoliko nije dozvoljava njegovo ažuriranje u bazi.
* Uraditi push koda
* Napraviti pull request

# Feature
Pregled nezaključenih narudžbi 
## Item
Seller App treba imati mogućnost pregledati narudzbe koje nisu zaključene.
### Task
* Kreirati end point
* Implementirati metodu koja vraća listu svih narudzbi koje nisu zaključene.
* Uraditi push koda
* Napraviti pull request

# Feature
Pregled svih sastojaka proizvoda 
## Item
Seller App treba imati mogućnost pregledati sve sastojke proizvoda.
### Task
* Kreirati end point
* Proširiti model proizova (dodati Description i Barcode)
* Implementirati metodu koja vraća listu svih proizvoda.
* Uraditi push koda
* Napraviti pull request

# Feature
Dobavljanje informacija o Cash Register-u
## Item
Cash Register treba biti u mogućnosti pristupiti svim informacijama o sebi neophodnim za generisanje QR koda.
### Task
* Kreirati end point
* Implementirati model CashRegister 
* Implementirati CashRegister controller
* Implementirati CashRegister service
* Implementirati CAshRegister repository
* Kreirati end point
* Implementirati metodu za vraćanje svih podataka o određenom Cash Register-u koje se čuvaju u bazi
* Uraditi push koda
* Napraviti pull request

# Feature
Promjena šifre
## Item
Seller mobilna App i Cash Register trebaju moći saznati kada je korisnikova šifra promijenjena od strane administratora i u tom slučaju po loginu treba ga odmah preusmjeriti na promjenu vlastite šifre radi sigurnosti.
### Task
* Proširiti model User sa informacijom o privremenoj šifri i reset tokenu
* Napraviti rutu na koju se šalje zahtjev s mailom-korisnika
* Implementirati provjeru maila i slanje reset tokena na mail
* Kreirati html formu maila
* Implementirati rutu na koju se šalje sigurnosni token kako bi se provjerila njegova ispravnost
* Implementirati metode za provjeru tokena
* Implementirati rutu za spremanje novog passworda
* Implementirati metode za promjenu passworda i pohranu u bazu
* Poslati nove informacije o Useru Main serveru
* Uraditi push koda
* Napraviti pull request

# Feature
Asinhrono primanje poruka
## Item
Korisnik Cash Register aplikacije i Seller mobilne aplikacije treba imati mogućnost asinhronog primanja poruka koje je poslao Guest user. 
### Task

* Uraditi push koda
* Napraviti pull request

# Feature
Pregled računa s uključenim PDV-om
## Item
 Korisnik Cash Register App treba imati mogućnost pregleda računa uz informaciju o PDV-u proizvoda. 
### Task
* Proširiti model proizvoda sa informacijom o PDV-u
* Uz dobavljanje podataka o proizvodu s Main servera pokupiti i podatak o PDV-u
* Uraditi push koda
* Napraviti pull request

# Feature
Grafički prikaz stolova
## Item
Korisnik Seller App i Cash Register App treba imati mogućnost uvida u prostorni razmještaj stolova/stolica/… u restoranu/ordinaciji/…
### Task
* Kreirati Table model
* Kreirati Table controller
* Kreirati Tebale service
* Kreirati Table repository
* Implementirati metodu za dobavljanje informacija o broju stolova za poslovnicu od Main servera
* Implementirati rutu za dobavljanja informacija o stolovima
* Uraditi push koda
* Napraviti pull request

# Feature
Filtriranje proizvoda 
## Item
Korisnik Seller App treba imati mogućnost filtriranja svih proizvoda po sastojcima.
### Task
* Implementirati metodu za dobvljanje svih proizvoda iz baze
* Uraditi push koda
* Napraviti pull request

# Feature
Konfiguracijske postavke aplikacija
## Item
Seller Mobilna App i Cash Register trebaju imati mogućnost dobavljanja informacija o konfiguracijskim postavkama (jezik, vrijeme).
### Task
* Dobaviti informacije o konfiguracijama od Main servera
* Implementirati rutu preko koje Cash Register server dobavlja informacije o konfiguracijama koje su mu potrebne
* Implementirati rutu preko koje Seller App dobavlja informacije o konfiguracijama koje su joj potrebne
* Uraditi push koda
* Napraviti pull request


