# Дипломный проект Яндекс.Практикума по курсу "Android-разработчик"

Минимально поддерживаемая приложением версия SDK — 26.
есть Legacy-иконка согласно дизайну.
Светлая и тёмная темы приложения наследуются от Theme.MaterialComponents.DayNight.NoActionBar.
В теме приложения описана цветовая палитра (светлая и тёмная) согласно предоставленному дизайну.
Ориентация приложения зафиксирована как portrait.
Навигация приложения реализована в подходе Single Activity с использованием Jetpack Navigation Component.
Все переходы между экранами реализованы с помощью NavController, а также BottomNavigationView.
анимации пока не делаем, решим это при оценке времени по результатам первого ревью
Для выполнения сетевых запросов используется библиотека Retrofit.
В компоненте, осуществляющем выполнение сетевых запросов , реализована логика предварительной проверки на наличие подключения к интернету.
Для внедрения зависимостей используется Koin.


на данный момент работает
-поиск,
-отправка письма,
-отправка ссылки на резюме, 
-звонок работодателю,
-добавление и удаление вакансии в избранное
-ввод желаемой зарплаты с сохранением после применения

пока не работает
- переходы по экранам филььров
- поиск по фильтрам
- нет пагинации поиска
- не доконца настроено отображение в темной теме деталей вакансии разбираемся с WEBVIEW. 
- не доконца настроено отображение деталей вакансии: не устранен баг с лого и ключевыми навыками

Мы очень старались значения цвета, отступов, размеров текста и строковые константы вынести в ресурсы, но больше чем уверены местами есть провалы.

на доске  декомпозиуия + оцегочный вес временных затрат
https://github.com/users/AntaOtk/projects/1/views/3
