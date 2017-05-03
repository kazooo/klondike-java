Projekt do predmetu IJA 2016/2017

Clenove tymu:
	Kiselevich Roman (xkisel00)
	Ermak Aleksei (xermak00)

Popis projektu:
	Implementace aplikaci Pasians (Solitaire) Klondike, ktera vychazi ze stejnojmenne karetni hry.
	Aplikace implementuje zakladni pravidla hry. Je mozne rozehrat az 4 hry soucasne
	Pokud je rozehrana pouze jedna hra, plocha grafického rozhraní obsahuje pouze tuto hru (jedno hraci platno)
	Pokud je rozehrana vice nez jedna hra, plocha grafickeho rozhrani se rozdeli do 4 dlazdic, kazda pro jednu hru (hraci platno),
	pri tom nevyuzite dlazdice nebudou nic obsahovat a pocet rozehranych her lze menit za behu
	Kazda hra umoznuje operaci undo (alespon pet tahu), rozehranou hru je mozne ulozit a znovu nacist.
	Aplikaci nabizi moznost napovědy tahu (na zadost zobrazi jake tahy je mozne provest).

	Pro realizaci projektu byla pouzita Java SE 8.
	Pro graficke uzivatelske rozhraní byla pouzita technologie JFC/Swing.
	Pro preklad a spusteni projektu pouziva se nastroj Ant.

Kompilace a spousteni:
	Nejdrive je treba spustit skript get-libs.sh ve slozce lib pro stazeni z internetu obrazku karet a jinych pozadavanych souboru.
	Aplikace se kompiluje nastrojem Ant, pomoci prikazu ant compile.
	Aplikace se spousti pomoci prikazu ant run.
	Build file pro nastroj Ant take umoznuje generovani programove dokumentace, ktera se uklada do adresare doc, prikazem ant doc.
