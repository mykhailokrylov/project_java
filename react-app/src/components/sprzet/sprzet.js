import { useState, useEffect } from "react";

import EquipCard from "./card";

const cards = [
  {
    title: "Wybór wędki",
    image: "./11.jpg",
    alt: "wedka",
    payload: (
      <>
        <h6>Długość wędki:</h6>
        <p>
          Długość wędki powinna być dostosowana do rodzaju połowu i warunków
          łowienia. Krótsze wędki są zazwyczaj bardziej precyzyjne i nadają się
          do łowienia na małe odległości, podczas gdy dłuższe wędki umożliwiają
          rzuty na większe odległości.
        </p>

        <h6>Wytrzymałość wędki:</h6>
        <p>
          Wytrzymałość wędki powinna być dostosowana do rozmiaru i gatunku ryb,
          których zamierzasz łowić. Dla mniejszych ryb wybierz wędki o mniejszej
          wytrzymałości, a dla większych ryb wybierz wędki o większej
          wytrzymałości.
        </p>

        <h6>Materiał wędki:</h6>
        <p>
          Wędki są wykonane z różnych materiałów, takich jak włókno węglowe,
          włókno szklane i kompozyty. Włókno węglowe jest lekkie, wytrzymałe i
          zapewnia dobrą czułość, dlatego jest powszechnie stosowane przez
          wędkarzy.
        </p>
      </>
    ),
  },

  {
    title: "Wybór kołowrotki",
    image: "./12.png",
    alt: "kolowrotka",
    payload: (
      <>
        <h6>Rozmiar kołowrotka:</h6>
        <p>
          Rozmiar kołowrotka powinien być dostosowany do rozmiaru i wagi wędki
          oraz gatunku ryb, które zamierzasz łowić. Kołowrotki o większej
          pojemności żyłki są bardziej odpowiednie do łowienia większych ryb.
        </p>
        <h6>System hamulcowy:</h6>
        <p>
          Sprawdź, czy kołowrotek ma skuteczny system hamulcowy, który umożliwia
          kontrolowanie wydłużania się żyłki podczas walki z rybą.
        </p>
        <h6>Gładkość nawijania:</h6>
        <p>
          Ważne jest, aby kołowrotek nawijał żyłkę równomiernie i płynnie, aby
          uniknąć plątaniny i problemy podczas holowania ryb.
        </p>
      </>
    ),
  },
  {
    title: "Wybór przynęt",
    image: "./13.jpg",
    alt: "przynet",
    payload: (
      <>
        <h6>Gatunek ryby:</h6>
        <p>
          Przynęty powinny być dopasowane do preferencji żywieniowych i
          zachowania gatunku ryby, którą chcesz złowić. Niektóre przynęty są
          bardziej skuteczne dla konkretnych gatunków ryb.
        </p>
        <h6>Technika połowu:</h6>
        <p>
          Wybierz przynętę, która pasuje do techniki połowu, którą zamierzasz
          używać. Na przykład, woblerki są skuteczne podczas spinningu, podczas
          gdy spławik i zanęta są powszechnie stosowane w łowieniu na spokojnej
          wodzie.
        </p>
        <h6>Warunki łowienia:</h6>
        <p>
          W zależności od pory roku, temperatury wody i innych warunków,
          niektóre przynęty mogą być bardziej skuteczne. Zasięgnij informacji
          lokalnych wędkarzy lub sklepów specjalizujących się w sprzęcie
          wędkarskim, aby dowiedzieć się, które przynęty są najbardziej
          odpowiednie w danym miejscu i czasie.
        </p>
      </>
    ),
  },
  {
    title: "Wybór innych akcesoriów",
    image: "./15.jpg",
    alt: "inne",
    payload: (
      <>
        <h6>Haki:</h6>
        <p>
          Wybierz odpowiednie rozmiary i rodzaje haków dla różnych gatunków ryb.
          Pamiętaj, że większe ryby wymagają mocniejszych haków.
        </p>
        <h6>Przypony i obciążniki:</h6>
        <p>
          Przypony i obciążniki są używane do zwiększenia skuteczności połowu.
          Przypony mogą zwiększyć trwałość zestawu, a obciążniki pomogą utrzymać
          przynętę na odpowiedniej głębokości.
        </p>
        <h6>Sygnalizatory brań:</h6>
        <p>
          Sygnalizatory brań są przydatne do wędkowania na spokojnej wodzie.
          Informują o chwyceniu przynęty przez rybę poprzez dźwięk lub światło.
        </p>
      </>
    ),
  },
];

function Sprzet() {
  const images = require.context("images", true);

  const [menuSelection, setMenuSelection] = useState(0);

  const card = cards[menuSelection];

  return (
    <div className="container p-5 mt-2" style={{ height: "100%" }}>
      <h1>Porady dotyczące wyboru odpowiedniego sprzętu</h1>
      <div className="containerview d-flex">
        <nav id="navbut">
          {cards.map((card, index) => {
            return (
              <button onClick={() => setMenuSelection(index)}>
                {card.title}
              </button>
            );
          })}
        </nav>

        <EquipCard title={card.title} src={images(card.image)} alt={card.alt}>
          {card.payload}
        </EquipCard>
      </div>
    </div>
  );
}

export default Sprzet;
