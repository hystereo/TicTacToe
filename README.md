# Gra Kółko i Krzyżyk z użyciem RMI

## Opis

Program implementuje grę w Kółko i Krzyżyk (Tic Tac Toe) w środowisku rozproszonym z wykorzystaniem RMI (Remote Method Invocation). Gra jest wieloosobowa, a dwóch graczy łączy się ze sobą, aby grać w grę. Gra może być obsługiwana zarówno w trybie konsolowym, jak i graficznym (GUI).

**Uwaga**: Projekt nie jest jeszcze ukończony i wymaga dalszych poprawek.

## Funkcje

- Gra toczy się na planszy 3x3.
- Gracze wykonują ruchy na przemian, wybierając współrzędne (x, y) na planszy.
- Gra sprawdza, czy któryś z graczy wygrał, lub czy nastąpił remis.
- Serwer obsługuje rejestrację graczy, obsługę ruchów oraz monitorowanie stanu gry.

## Struktura

- **TicTacToeServer** - Interfejs serwera RMI, który udostępnia metody do rejestracji graczy, wykonywania ruchów i sprawdzania zwycięzcy.
- **TicTacToeServerImpl** - Implementacja serwera RMI, który zarządza stanem gry, obsługuje ruchy i sprawdza zwycięzcę.
- **TicTacToeClient** - Klient GUI, który umożliwia graczowi wykonywanie ruchów na planszy i wyświetlanie stanu gry.
- **TicTacToeConsole** - Klient konsolowy, który pozwala graczowi grać w grę w terminalu, zamiast w interfejsie graficznym.
- **BoardUpdater** - Interfejs umożliwiający aktualizowanie stanu planszy w różnych klientach.
- **BoardUpdaterRunnable** - Klasa odpowiedzialna za regularną aktualizację planszy w klientach.
