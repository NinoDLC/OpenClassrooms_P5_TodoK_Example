# Démo
![Démo Gif, wait for it !](https://github.com/NinoDLC/OpenClassrooms_P5_TodoK_Example/blob/master/example.gif)

# Sujets abordés / démontrés
 * Kotlin
 * Architecture MVVM (Model View ViewModel)
 * `LiveData` (en particulier le 'pont' `liveData {}` vers les coroutines)
 * `Coroutine` & `Flow`
 * Partie `ui`: `ViewBinding`
 * Partie `domain`: `Usecases` & `Entities`
 * Partie `data`: `Repository` & `Dao Room`
 * Injection de dépendance (DI) avec `Hilt`
 * Tests unitaires (TU) avec des `LiveData`, `Coroutines` et `Flows` (grâce à `MockK`)
 * Code Coverage entre supérieur à 93% (report disponible grâce à `Kover`)

# Commandes utiles :
`./gradlew koverHtmlReportDebug` pour générer le rapport de coverage des tests unitaires
