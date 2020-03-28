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
