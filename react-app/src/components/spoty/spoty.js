import image7 from "images/7.jpg";
import image8 from "images/8.jpg";
import image9 from "images/9.jpg";
import image10 from "images/10.jpg";

function Spoty() {
  return (
    <div className="container p-5 mt-2">
      {/* <h4>
        Poszukaj najbliższego zbiornika wodnego i pospiesz się łowić ryby!
      </h4>
      <br />
      <p>
        <button
          //TODO:   onClick="getLocation()"
          style={{
            backgroundColor: "#669966",
            border: "2px solid white",
            color: "white",
          }}
        >
          Pokaż moją lokalizację
        </button>
      </p>

      <div className="container p-3 mb-4">
        <div id="mapka" style={{ width: "auto", height: "300px" }}></div>
      </div> */}
      <h4>Miejsca połowów dla początkujących wędkarzy</h4>
      <br />
      <p>
        Oto kilka sugestii dotyczących miejsc połowów, które mogą być idealne
        dla Ciebie:
      </p>
      <ul>
        <li>
          <strong>Jeziora:</strong> Jeziora są popularnym wyborem dla
          początkujących wędkarzy ze względu na swoją dostępność i różnorodność
          ryb. Możesz znaleźć jeziora zarówno w pobliżu miasta, jak i na
          obszarach wiejskich. Wiele z nich jest łatwo dostępnych, posiada
          miejsca do wędkowania z brzegu i może być dobrze zaopatrzone w ryby.
          <div className="row justify-content-center">
            <div className="col-md-6 my-3">
              <img src={image7} alt="1" className="img-fluid" />
            </div>
          </div>
        </li>
        <li>
          <strong>Rzeki i strumienie:</strong> Rzeki i strumienie są doskonałymi
          miejscami do połowu, zwłaszcza jeśli szukasz ryb płoszowych, takich
          jak pstrągi czy lipienie. Ważne jest jednak, aby wybrać odpowiednie
          miejsce, gdzie możesz łatwo dotrzeć do brzegu i znaleźć odpowiednie
          miejsce na wędkowanie.
          <div className="row justify-content-center">
            <div className="col-md-6 my-3">
              <img src={image8} alt="1" className="img-fluid" />
            </div>
          </div>
        </li>
        <li>
          <strong>Molo lub pomosty:</strong> Molo lub pomosty nad jeziorami,
          rzekami czy nadmorskimi obszarami mogą być doskonałymi miejscami dla
          początkujących wędkarzy. Zapewniają one wygodne miejsce do wędkowania
          i często są dobrze zarybione. Pamiętaj jednak, aby sprawdzić zasady i
          przepisy dotyczące połowu na danym molo lub pomostach.
          <div className="row justify-content-center">
            <div className="col-md-6 my-3">
              <img src={image9} alt="1" className="img-fluid" />
            </div>
          </div>
        </li>
        <li>
          <strong>Zbiorniki retencyjne:</strong> Zbiorniki retencyjne to
          sztuczne zbiorniki wodne, które mogą być doskonałymi miejscami do
          połowu dla początkujących wędkarzy. Są one często zaopatrzone w różne
          gatunki ryb i oferują łatwy dostęp z brzegu.
          <div className="row justify-content-center">
            <div className="col-md-6 my-5">
              <img src={image10} alt="1" className="img-fluid" />
            </div>
          </div>
        </li>
      </ul>
      <p>
        Pamiętaj, że przed wyruszeniem na połowy zawsze warto sprawdzić przepisy
        i zasady obowiązujące w danym miejscu. Nie zapomnij również o licencji
        wędkarskiej, która może być wymagana w niektórych obszarach.
      </p>
    </div>
  );
}

export default Spoty;
