# Currency

## Here is a program that fetches exchange rates from several banks and calculates their average value in several optimized ways each time.
* CBU -> this version is written only to get courses from cbu bank. \
  Used technologies 
  * RestTemplate
  * JPA
  * Mupstruct
  * Intigration tesing with mockito
* CBU_Cashing -> This version has added data caching future to the previous version and the testing is optimized
  * Ehcache
* CCY_Pattern -> in the last version, the structure has been modified and Stratage and Composite patterns have been added from Disegn patterns. The first and second versions of the Composite design pattern differ in how they consume Services
