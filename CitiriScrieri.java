Cititre din fisier text:
public static List<Produs> citesteProduse(String caleFisier){
    List<Produs>produse=new ArrayList<>();

    try(BufferedReader br=new BufferedReader(new FileReader(caleFisier))) {
        String linie;

        while((linie=br.readLine())!=null){

            String[] parts=linie.split(",");

            int cod= Integer.parseInt(parts[0].trim());
            String denumire=parts[1].trim();
            double pret=Double.parseDouble(parts[2].trim());

            produse.add(new Produs(cod,denumire,pret));

        }

    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
    return produse;
}
Citire din fisier binar:
    public static DateCitite citesteDate(String caleFisier) {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(caleFisier))) {
            int nr = dis.readInt();
            double val = dis.readDouble();
            String text = dis.readUTF();

            return new DateCitite(nr, val, text);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

Citire din JSON:
public static List<Specializare> citesteJson(String numeFisier){

    List<Specializare>specializari=new ArrayList<>();

    try(FileInputStream fisierJson=new FileInputStream(numeFisier)) {

        JSONTokener jsonTokener=new JSONTokener(fisierJson);
        JSONArray specializariJson=new JSONArray(jsonTokener);

        for(int i=0; i<specializariJson.length();i++){
            JSONObject specializare=specializariJson.getJSONObject(i);
            String specialitate= specializare.getString("specialitate");


            List<Manevra>manevre=new ArrayList<>();
            JSONArray manevreJson=specializare.getJSONArray("manevre");

            for(int j=0; j<manevreJson.length();j++){
                JSONObject manevra=manevreJson.getJSONObject(j);

                Manevra m=new Manevra(
                        manevra.getInt("cod"),
                        manevra.getInt("durata"),
                        manevra.getDouble("tarif")
                );
                manevre.add(m);
            }

           Specializare s=new Specializare(specialitate,manevre);
           specializari.add(s);

        }

    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
    return specializari;
}

Citire din baza de date:

public static List<Specializare> citesteDinBD(){
    List<Specializare>specializari=new ArrayList<>();
    try(Connection con= DriverManager.getConnection("jdbc:sqlite:date/facultate.db");
        Statement cmd=con.createStatement();
        ResultSet result=cmd.executeQuery("SELECT * FROM specializari")) {
       while(result.next()){
           int cod=result.getInt(1);
           String denumire=result.getString(2);
           int locuri=result.getInt(3);

           Specializare s=new Specializare(cod, denumire, locuri);
           specializari.add(s);
       }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return specializari;
}
Citire din XML:

public static List<Obiect> citireXml(String caleFisier) throws Exception {
    List<Obiect> rezultat = new ArrayList<>();

    // 1. Inițializare parser DOM
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document document = builder.parse(caleFisier);
    Element radacina = document.getDocumentElement();

    // 2. Obținem lista de noduri principale (ex: produs, specialitate etc.)
    NodeList noduriObiect = radacina.getElementsByTagName("obiect"); // ex: "produs"

    for (int i = 0; i < noduriObiect.getLength(); i++) {
        Element nodObiect = (Element) noduriObiect.item(i);

        // 3. Citim sub-elemente (ex: cod, denumire)
        int cod = Integer.parseInt(nodObiect.getElementsByTagName("cod").item(0).getTextContent());
        String denumire = nodObiect.getElementsByTagName("denumire").item(0).getTextContent();

        // 4. Citim lista de sub-elemente (ex: tranzactii, manevre etc.)
        Element nodLista = (Element) nodObiect.getElementsByTagName("listaSub").item(0); // ex: "tranzactii"
        List<SubElement> subElemente = new ArrayList<>();

        NodeList noduriSub = nodLista.getElementsByTagName("subelement"); // ex: "tranzactie"
        for (int j = 0; j < noduriSub.getLength(); j++) {
            Element nodSub = (Element) noduriSub.item(j);
            String atribut1 = nodSub.getAttribute("atribut1");
            int atribut2 = Integer.parseInt(nodSub.getAttribute("atribut2"));

            SubElement sub = new SubElement(atribut1, atribut2); // adaptează clasa ta aici
            subElemente.add(sub);
        }

        // 5. Construim obiectul principal și îl adăugăm în listă
        rezultat.add(new Obiect(cod, denumire, subElemente)); // adaptează constructorul
    }

    return rezultat;
}








Scriere in fisiert text:

public static void scrieInText(String numeFisier, List<String> linii) {
        try (PrintWriter out = new PrintWriter(new FileWriter(numeFisier))) {
            for (String linie : linii) {
                out.println(linie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

Scriere in fisier binar:

public static void scrieInBinar(String numeFisier, List<Persoana> persoane) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(numeFisier))) {
            out.writeObject(persoane); // scrie întreaga listă
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




Scriere in fisier json:
SIMPLU:
public static void scrieStudenti(String numeFisier, List<Student> studenti) {
        JSONArray array = new JSONArray();

        for (Student s : studenti) {
            JSONObject obj = new JSONObject();
            obj.put("nume", s.getNume());
            obj.put("varsta", s.getVarsta());
            array.put(obj);
        }

        try (PrintWriter out = new PrintWriter(numeFisier)) {
            out.print(array.toString(4)); // indentare
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
IMBRICAT:
public static void scrieProduse(String numeFisier, List<Produs> produse) {
        JSONArray array = new JSONArray();

        for (Produs p : produse) {
            JSONObject produsJson = new JSONObject();
            produsJson.put("cod", p.getCod());
            produsJson.put("denumire", p.getDenumire());

            JSONArray tranzactiiJson = new JSONArray();
            for (Tranzactie t : p.getTranzactii()) {
                JSONObject tJson = new JSONObject();
                tJson.put("tip", t.getTip().toString());
                tJson.put("cantitate", t.getCantitate());
                tranzactiiJson.put(tJson);
            }

            produsJson.put("tranzactii", tranzactiiJson);
            array.put(produsJson);
        }

        try (PrintWriter out = new PrintWriter(numeFisier)) {
            out.print(array.toString(4));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

Scriere in XML:
public static void scrieXML(String numeFisier) {
        try {
            // 1. Creăm documentul XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // 2. Creăm elementul rădăcină
            Element root = doc.createElement("radacina"); // ex: "medicale"
            doc.appendChild(root);

            // 3. Adăugăm elemente
            for (int i = 0; i < 3; i++) {
                Element obiect = doc.createElement("obiect"); // ex: "specialitate"

                Element nume = doc.createElement("nume");
                nume.setTextContent("Obiect " + i);
                obiect.appendChild(nume);

                Element sublista = doc.createElement("subelemente"); // ex: "manevre"

                for (int j = 0; j < 2; j++) {
                    Element sub = doc.createElement("sub");
                    sub.setAttribute("atribut1", String.valueOf(j));
                    sub.setAttribute("atribut2", "valoare");
                    sublista.appendChild(sub);
                }

                obiect.appendChild(sublista);
                root.appendChild(obiect);
            }

            // 4. Scriem XML-ul în fișier
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // formatare frumoasă
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(numeFisier));
            transformer.transform(source, result);

            System.out.println("✅ Fișier XML creat: " + numeFisier);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

