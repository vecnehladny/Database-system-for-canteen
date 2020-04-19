# Jedáleň - Databázové systémy

## Popis

Semestrálnemu projek z predmetu Databázové systémy. Projekt sa zaoberá vývojom aplikácie, ktorá implementuje databázy v plnom rozsahu na zadanú rámcovú tému. Hlavnou náplňou je naprogramovať funkčnú aplikáciu, ktorá bude vedieť vykonávať základné operácie s databázou a to napríklad, vyberanie, vkladanie a pod.

## Funkcie
**Pridávanie záznamov**

Jedná sa o registráciu používateľov a následné prihlásenie na úvodnej obrazovke, pred samotným vstupom do aplikácie. Registrovanie prebieha tým, že užívateľ si vyberie možnosť, že sa chce registrovať a následne vyplní formulár s potrebnými údajmi. Následne odošle formulár. Aplikácia sa postará o spracovanie týchto informácií a to tak, že skontroluje, či užívateľ už nie je registrovaný, v tom prípade ho presunie na prihlasovaciu obrazovku, ak teda nie je, tak vloží záznam do tabuľky užívateľov so všetkými informáciami a užívateľ je pripravený sa prihlásiť. Proces prihlasovania prebieha, vyplnením údajov a ich potvrdenie, aplikácia skontroluje, či existuje záznam o užívateľovi s danými prihlasovacími údajmi, ak existuje tak skontroluje či sa zhodujú a tak isto skontroluje aké privilégiá užívateľ má. Ak sa údaje zhodujú tak sa užívateľa prihlási do určeného módu podľa spomínaných právomocí a presunie na domovskú stránku aplikácie.

**Vyhľadávanie záznamov**

Vyhľadávanie popisujeme v 2. scenári. Užívateľ má na domovskej stránke zoznam dostupných jedál v ktorom môže vyhľadávať, tieto jedlá si môže pridávať v ľubovoľnom počte do košíka. Obsah košíka si môže následne prezrieť a vytvoriť z neho objednávku. Aplikácie tento proces spracováva postupne, najprv vypíše na obrazovku zoznam dostupných jedál. V tomto zozname môže používateľ tak isto vyhľadávať, takže aplikácia zabezpečí relevantný výsledok pre zadaný dopyt. Vyhľadá vhodný záznam v tabuľke s jedlami a vypíše, tak isto ako vyhľadá príslušné záznamy so surovinami, z akých sa dané jedlo skladá a kto je kuchárom daného jedla. Takto skompletizované údaje vypíše na obrazovku používateľovi.

**Uprava záznamov**

V našej aplikácií používame aj privilegovaných užívateľov, tzv. adminov. Takýto typ používateľa ma oprávnenia na upravovanie záznamov, či už o jedlách alebo objednávkach, ale taktiež má právomoc upravovať právomoci iných užívateľov. Admin si vie nechať vypísať zoznam všetkých používateľov a môže sa rozhodnúť, komu pridelí právomoci admina alebo naopak, kto stratí privilégia byť adminom. Aplikácia sa postará o vyhľadanie záznamu daného používateľa a následne upraví boolean hodnotu označenia používateľových privilégií. Takto upravený používateľ je následne pripravený prihlásiť sa s danými právomocami.

**Filtrovanie záznamov**

Používateľ s právomocami admina vie prezerať jednotlivé objednávky. V týchto objednávkach vie tak isto vyhľadávať a filtrovať ich podľa rôznych kritérií, kde sa aplikácia stará o dané filtrovanie na úrovni databázy. Filtrovanie prebieha optimálne ku danému počtu záznamov. 

**Odstránenie záznamov z tabuľky**

Používateľ s oprávneniami dokáže okrem prezeranie jednotlivých objednávok, tieto objednávky aj vymazávať, ak je to potrebné. Aplikácia musí v taktom prípade vhodne vyhľadať záznam objednávky a tak isto aj ku nemu všetky prislúchajúce záznami v iných tabuľkách ako napríklad faktúry alebo obsah objednávky v podobe jedla. Takto vyhľadané údaje sa odstránia a žiadny používateľ ku nim teda už nemá prístup.

 
## Inštalácia
1. Naklonuj repozitár
```
git clone https://github.com/FIIT-DBS2020/project-klima_mikuska
```
2. Nastav spojenie s databázov

## Požiadavky
1. mysql
```
brew install mysql
```
2. java
3. javafx
