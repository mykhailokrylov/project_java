import image1 from "images/1.jpg";
import image2 from "images/2.jpg";

function Home() {
  return (
    <div className="container p-5 mt-2">
      <div className="container pb-5">
        <div className="row">
          <div className="col-md-6">
            <img src={image1} alt="1" className="img-fluid" />
          </div>
          <div className="col-md-6">
            <img src={image2} alt="1" className="img-fluid" />
          </div>
        </div>
      </div>
      <h2>Wędkarz Na Start: Informacje i Porady dla Początkujących</h2>
      <p>
        Witamy na naszej stronie dla początkujących wędkarzy! Jeśli jesteś
        nowicjuszem w świecie wędkarstwa lub chcesz rozpocząć swoją przygodę z
        łowieniem ryb, jesteś we właściwym miejscu. Nasza strona oferuje szeroką
        gamę informacji, porad i wskazówek, które pomogą Ci stać się optymalnym
        wędkarzem.
      </p>
      <br />
      <h3>Co znajdziesz na naszej stronie?</h3>
      <ol>
        <li>
          <h4>Podstawowe techniki wędkarskie</h4>
          <p>
            Dowiedz się, jak poprawnie korzystać z wędki, kołowrotka i innych
            podstawowych narzędzi. Oferujemy instrukcje krok po kroku, które
            pozwolą Ci zacząć łowić ryby efektywnie i skutecznie.
          </p>
        </li>
        <li>
          <h4>Wybór sprzętu</h4>
          <p>
            Zobacz nasze porady dotyczące wyboru odpowiedniej wędki, kołowrotka,
            przynęt i innych akcesoriów wędkarskich. Dowiedz się, jak dopasować
            sprzęt do konkretnych gatunków ryb i warunków łowienia.
          </p>
        </li>
        <li>
          <h4>Informacje o rybach</h4>
          <p>
            Poznaj różne gatunki ryb, które możesz spotkać podczas wędkowania.
            Dowiedz się o ich zwyczajach, preferencjach żywieniowych i
            najlepszych technikach łowienia.
          </p>
        </li>
        <li>
          <h4>Porady dotyczące miejsc łowienia</h4>
          <p>
            Oferujemy wskazówki dotyczące najlepszych miejsc do wędkowania, w
            tym jeziora, rzeki, stawy i inne spoty wędkarskie. Dowiedz się, jak
            znaleźć odpowiednie miejsce, uwzględniając rodzaj ryb, sezon i
            warunki pogodowe.
          </p>
        </li>
        <li>
          <h4>Porady ogólne</h4>
          <p>
            Nie tylko dostarczamy informacji technicznych, ale również udzielamy
            porad ogólnych dotyczących przygotowania się do wędkowania,
            pakowania sprzętu, bezpieczeństwa na wodzie i wielu innych aspektów.
          </p>
        </li>
      </ol>

      <p>
        Nie trać czasu i dołącz do naszej społeczności wędkarskiej. Zacznij
        swoją wędkarską przygodę już dziś i odkryj radość i satysfakcję, jaką
        daje połów ryb. Zapraszamy do eksploracji naszej strony i czerpania z
        niej wartościowych informacji i wskazówek!
      </p>
    </div>
  );
}

export default Home;
