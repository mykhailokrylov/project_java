function EquipCard(props) {
  const { title, children, src, alt } = props;

  return (
    <section id="blok">
      <h2>{title}</h2>
      <p className="srodek">
        <img
          src={src}
          alt={alt}
          className="img-fluid"
          style={{ maxHeight: "200px" }}
        />
      </p>
      {children}
    </section>
  );
}

export default EquipCard;
