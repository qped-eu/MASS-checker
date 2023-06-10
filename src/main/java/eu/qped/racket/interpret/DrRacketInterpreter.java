package eu.qped.racket.interpret;

import eu.qped.Temp.DrRacketLexer;
import eu.qped.Temp.DrRacketParser;
import eu.qped.racket.functions.CustomFunction;
import eu.qped.racket.functions.booleans.*;
import eu.qped.racket.functions.lists.*;
import eu.qped.racket.functions.numbers.*;
import eu.qped.racket.functions.numbers.Random;
import eu.qped.racket.buildingBlocks.*;
import eu.qped.racket.buildingBlocks.Boolean;
import eu.qped.racket.buildingBlocks.Number;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ANTLRErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.LinkedList;
import java.util.List;

public class DrRacketInterpreter {

	private static final String DEFAULT_XQUERY_FILE = "interpret.xqy";
	private boolean parseErrorsOccurred;
	private String errorOutput;
	private String xml;
	private String rktFile;
	private Expression expression;
	private List<Expression> expressionList;	//alle expressions, die gegeben wurden
	private List<CustomFunction> customFunctionList = new LinkedList<>();

	public DrRacketInterpreter(String rktFile) throws Exception {

		this.rktFile = rktFile;

		DrRacketLexer lexer = new DrRacketLexer(CharStreams.fromString(rktFile));

		DrRacketParser parser = new DrRacketParser(new CommonTokenStream(lexer));

		ANTLRErrorStrategy errorStrategy = new DefaultErrorStrategy() {
			@Override
			public void reportError(Parser recognizer, RecognitionException e) {
				super.reportError(recognizer, e);
				parseErrorsOccurred = true;
			}

		};
		parser.setErrorHandler(errorStrategy);
		PrintStream origSysErr = System.err;

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try (PrintStream ps = new PrintStream(bos)) {
			System.setErr(ps);
			parser.start();
			System.setErr(origSysErr);
		}

		errorOutput = bos.toString();
		if (parseErrorsOccurred) {
			throw new Exception("Ein Fehler ist beim Einlesen der DrRacket-Datei aufgetreten. "
					+ "Vermutlich ist die Datei nicht im Text-Format gespeichert. "
					+ "Das passiert z.B. dann, wenn Bilder Teil des Programms sind. "
					+ "Probieren Sie die Datei über 'File' -> 'Save Other' -> 'Save Definitions as Text ...' zu speichern. "
					+ "Bilder gehen dabei verloren und Berechnungen, die von den Bildern abhängen werden dadurch vorraussichtlich fehlerhaft.");
		}

		// pretty printing
		xml = prettyPrint(parser.xml.toString());

	}

//	public String evaluateExpressions() {
//		return expression.evaluate(new Expression());
//	}

	public String evaluateExpressions() throws Exception {
		return expression.evaluate(new Expression()).toString();
	}

//	public List<String> getAllExpressionEvaluations() {
//		return expressionList.stream().map(x -> x.evaluate(new Expression())).collect(Collectors.toList());
//	}

	public List<Object> getAllExpressionEvaluations() {
		return expressionList.stream().map(x -> {
			try {
				return x.evaluate(new Expression());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}).collect(Collectors.toList());
	}

	private String prettyPrint(String xml) throws SAXException, IOException, ParserConfigurationException,
			TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException {
		InputSource src = new InputSource(new StringReader(xml.toString()));
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		Writer out = new StringWriter();
		transformer.transform(new DOMSource(document), new StreamResult(out));
		return out.toString();
	}

	//Everything we commented "out" is not used in this version ,but it might be helpful for future Addons

	/**
	 * Interpret the Racket program with the XQuery expression from the default file.
	 * @return
	 * @throws Exception
	 */
	/*
	public String interpretWithXQuery() throws Exception {
		// read the XQuery expression from a file on the class path (e.g., the src/main/resources folder)
		String query = IOUtils.toString(ClassLoader.getSystemResourceAsStream(DEFAULT_XQUERY_FILE), Charset.defaultCharset());

		return interpretWithXQuery(query);
	}

	 */

	/**
	 * Interpret the Racket program with the passed XQuery expression.
	 * @return
	 * @throws Exception
	 */
	/*
	public String interpretWithXQuery(String query) throws SaxonApiException, Exception, IOException {
		// prepare query execution with Saxon:
		Processor processor = new Processor(Configuration.newConfiguration());

		// create parsed XML document
		InputSource is = new InputSource(new StringReader(xml));
		DocumentBuilder builder = processor.newDocumentBuilder();
		XdmNode doc = builder.build(new SAXSource(is));

		
		XQueryCompiler compiler = processor.newXQueryCompiler();

		// prepare XQuery evaluation		
		XQueryExecutable exp = compiler.compile(query);
		final XQueryEvaluator evaluator = exp.load();
		evaluator.setContextItem(doc);

		// check if XQuery expression is correct
		if (!exp.getUnderlyingCompiledQuery().usesContextItem()) {
			throw new Exception("Fehlerhafter Check (XQuery verwendet Context-Item nicht).");
		}

		// prepare serializer for result of XQuery expreession
		// the result will be written to bos
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Serializer serializer = processor.newSerializer(bos);
		serializer.setOutputProperty(Serializer.Property.METHOD, "adaptive");
	
		// execute query
		evaluator.run(serializer);
		
		// collect result
		bos.flush();
		String result = bos.toString();
		bos.close();
		
		// done
		return result;
	}

	 */

	public String getInput() {
		return rktFile;
	}

	public String getXml() {
		return xml;
	}

	public boolean hasParseError() {
		return parseErrorsOccurred;
	}
	
	public String getParseErrors() {
		if (parseErrorsOccurred)
			return errorOutput;
		else
			return "No parse errors.";	
	}

	private static void removeText (NodeList children) {
		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i).getNodeName().equals("#text")) {
				children.item(i).getParentNode().removeChild(children.item(i));
			}
		}
	}

	private Expression goDeeper(NodeList nodeList) {
		Expression expression = new Expression();

		for (int j = 0; j < nodeList.getLength(); j++) {
			NamedNodeMap inside = nodeList.item(j).getAttributes();
			String typeString = inside.getNamedItem("type").toString();
			if (typeString.compareTo("type=\"round\"") == 0) {	//TODO andere Klammern?
				removeText(nodeList.item(j).getChildNodes());

				expression.addPart(goDeeper(nodeList.item(j).getChildNodes()));
				continue;
			}

			System.out.println("\tname is : " + nodeList.item(j).getNodeName() + "( " + inside.getNamedItem("type") + " | " + inside.getNamedItem("value") + " )");
			String valueString = inside.getNamedItem("value").toString();



			//On Custom function
			if (valueString.compareTo("value=\"define\"") == 0) {	//Define
				customFunction(nodeList.item(j).getParentNode(), j);
				System.out.println("Finished Custom Function");
				j += 2;
				continue;
			}

			if (typeString.compareTo("type=\"Name\"") == 0) {
				if (!typeName(valueString, expression)) {	//Prüfen, ob es einen Typen gibt
					if (customFunctionList.stream().filter(x -> x.getFunName().compareTo(valueString.substring(7,valueString.length()-1)) == 0).findFirst().orElse(null) == null) {	//Ist es keine Custom Function, dann ist es ein Parameter
						expression.addPart(new Parameter(valueString.substring(7, valueString.length() - 1)));
					} else { //Ansosnten customFunction
						expression.addPart(customFunctionList.stream().filter(x -> x.getFunName().compareTo(valueString.substring(7,valueString.length()-1)) == 0).findFirst().orElse(null));
					}

				}

			}
			if (typeString.compareTo("type=\"Number\"") == 0) {
				expression.addPart(new Number(Float.valueOf(valueString.substring(7,valueString.length()-1))));
			}
			if (typeString.compareTo("type=\"Boolean\"") == 0) {
				expression.addPart(new Boolean(java.lang.Boolean.valueOf(valueString.substring(7,valueString.length()-1))));
			}

		}

		return expression;
	}


	private Expression customFunction(Node node, int number) {
		System.out.println("Start Custom Function");
		Expression expression = new Expression();
		String funName = "";
		List<Parameter> parameterList = new LinkedList<>();
		Expression body = new Expression();

		NodeList nodeList = node.getChildNodes();
		removeText(nodeList);

		NodeList headerNodeChildren = nodeList.item(number+1).getChildNodes();
		removeText(headerNodeChildren);
		NodeList bodyNodeChildren = nodeList.item(number+2).getChildNodes();
		removeText(bodyNodeChildren);


		System.out.println("Headder: ");

		for (int i = 0; i < headerNodeChildren.getLength(); i++) {
			NamedNodeMap inside = headerNodeChildren.item(i).getAttributes();
			String valueString = inside.getNamedItem("value").toString();
			String typeString = inside.getNamedItem("type").toString();
			System.out.println("\tname is : " + headerNodeChildren.item(i).getNodeName() + "( " + typeString + " | " + valueString + " )");
			if (funName == "") {
				funName = valueString.substring(7,valueString.length()-1);
			} else {
				parameterList.add(new Parameter(valueString.substring(7,valueString.length()-1)));
			}
		}

		System.out.println("Body: ");

		body = goDeeper(bodyNodeChildren);




		System.out.println("FunName: " + funName);
		System.out.println("ParameterList: " + parameterList);
		System.out.println("Body: " + body);
		CustomFunction customFunction = new CustomFunction(funName, parameterList, body);
		this.customFunctionList.add(customFunction);
		return customFunction;
	}

	/**
	 * Builds and Evaluates the expression
	 * @throws Exception
	 */
	public void evaluate() throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new InputSource(new StringReader(xml)));

		Element root = document.getDocumentElement();
		NodeList children = root.getChildNodes();

		removeText(children);
		Expression rootExpression = new Expression();

		for (int i = 0; i < children.getLength(); i++) {
			System.out.println("name is : " + children.item(i).getNodeName());
			NodeList c2 = children.item(i).getChildNodes();
			removeText(c2);
			if (!goDeeper(c2).getParts().isEmpty())
				rootExpression.addPart(goDeeper(c2));
		}
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println(rootExpression);
		//System.out.println(rootExpression.evaluate(new Expression()));
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		expression = rootExpression;
		expressionList = rootExpression.getParts();
	}

	private static boolean typeName(String valueString, Expression expression) {
		//On Numbers
		if (valueString.compareTo("value=\"+\"") == 0) {
			expression.addPart(new Plus());
			return true;
		}
		if (valueString.compareTo("value=\"-\"") == 0) {
			expression.addPart(new Minus());
			return true;
		}
		if (valueString.compareTo("value=\"*\"") == 0) {
			expression.addPart(new Multiplication());
			return true;
		}
		if (valueString.compareTo("value=\"/\"") == 0) {
			expression.addPart(new Division());
			return true;
		}
		if (valueString.compareTo("value=\"<\"") == 0) {	//Less Than
			expression.addPart(new LessThan());
			return true;
		}
		if (valueString.compareTo("value=\">\"") == 0) {	//Greater Than
			expression.addPart(new GreaterThan());
			return true;
		}
		if ((valueString.compareTo("value=\"=\"") == 0) || (valueString.compareTo("value=\"equal?\"") == 0)) {	//Equal
			expression.addPart(new Equal());
			return true;
		}
		if (valueString.compareTo("value=\"<=\"") == 0) {	//Lesser Or Equal Than
			expression.addPart(new LessOrEqualThan());
			return true;
		}
		if (valueString.compareTo("value=\">=\"") == 0) {	//Greater Or Equal Than
			expression.addPart(new GreaterOrEqualThan());
			return true;
		}
		if (valueString.compareTo("value=\"abs\"") == 0) {	//Absolute
			expression.addPart(new Absolute());
			return true;
		}
		if (valueString.compareTo("value=\"add1\"") == 0) {	//Add1
			expression.addPart(new Add1());
			return true;
		}
		if (valueString.compareTo("value=\"ceiling\"") == 0) {	//Ceiling
			expression.addPart(new Ceiling());
			return true;
		}
		if (valueString.compareTo("value=\"even?\"") == 0) {	//Even
			expression.addPart(new Even());
			return true;
		}
		if (valueString.compareTo("value=\"exp\"") == 0) {	//Exp
			expression.addPart(new Exp());
			return true;
		}
		if (valueString.compareTo("value=\"floor\"") == 0) {	//Floor
			expression.addPart(new Floor());
			return true;
		}
		if (valueString.compareTo("value=\"log\"") == 0) {	//Log
			expression.addPart(new Log());
			return true;
		}
		if (valueString.compareTo("value=\"max\"") == 0) {	//Max
			expression.addPart(new Max());
			return true;
		}
		if (valueString.compareTo("value=\"min\"") == 0) {	//Min
			expression.addPart(new Min());
			return true;
		}
		if (valueString.compareTo("value=\"modulo\"") == 0) {	//Modulo
			expression.addPart(new Modulo());
			return true;
		}
		if (valueString.compareTo("value=\"negative?\"") == 0) {	//Negative
			expression.addPart(new Negative());
			return true;
		}
		if (valueString.compareTo("value=\"odd?\"") == 0) {	//Odd
			expression.addPart(new Odd());
			return true;
		}
		if (valueString.compareTo("value=\"positive?\"") == 0) {	//Positive
			expression.addPart(new Positive());
			return true;
		}
		if (valueString.compareTo("value=\"random\"") == 0) {	//Random
			expression.addPart(new Random());
			return true;
		}
		if (valueString.compareTo("value=\"round\"") == 0) {	//Round
			expression.addPart(new Round());
			return true;
		}
		if (valueString.compareTo("value=\"sqr\"") == 0) {	//Sqr
			expression.addPart(new Sqr());
			return true;
		}
		if (valueString.compareTo("value=\"sqrt\"") == 0) {	//Sqrt
			expression.addPart(new Sqrt());
			return true;
		}
		if (valueString.compareTo("value=\"sub1\"") == 0) {	//Sqrt
			expression.addPart(new Sub1());
			return true;
		}
		if (valueString.compareTo("value=\"zero?\"") == 0) {	//Zero?
			expression.addPart(new Zero());
			return true;
		}
		//On Boolean
		if (valueString.compareTo("value=\"boolean=?\"") == 0) {	//Boolean=?
			expression.addPart(new BooleanEQ());
			return true;
		}
		if (valueString.compareTo("value=\"boolean?\"") == 0) {	//Boolean?
			expression.addPart(new BooleanQ());
			return true;
		}
		if (valueString.compareTo("value=\"false?\"") == 0) {	//False?
			expression.addPart(new FalseQ());
			return true;
		}
		if (valueString.compareTo("value=\"not\"") == 0) {	//Not
			expression.addPart(new Not());
			return true;
		}
		//Lists
		if (valueString.compareTo("value=\"cons\"") == 0) {	//Cons
			expression.addPart(new Cons());
			return true;
		}
		if (valueString.compareTo("value=\"append\"") == 0) {	//Append
			expression.addPart(new Append());
			return true;
		}
		if (valueString.compareTo("value=\"empty\"") == 0) {	//Empty
			expression.addPart(new Empty());
			return true;
		}
		if (valueString.compareTo("value=\"empty?\"") == 0) {	//Empty?
			expression.addPart(new EmptyQ());
			return true;
		}
		if (valueString.compareTo("value=\"first\"") == 0) {	//First
			expression.addPart(new First());
			return true;
		}
		if (valueString.compareTo("value=\"length\"") == 0) {	//Length
			expression.addPart(new Length());
			return true;
		}
		if (valueString.compareTo("value=\"list\"") == 0) {	//List
			expression.addPart(new eu.qped.racket.functions.lists.List());
			return true;
		}
		if (valueString.compareTo("value=\"member\"") == 0) {	//Member
			expression.addPart(new Member());
			return true;
		}
		if (valueString.compareTo("value=\"member?\"") == 0) {	//Member?
			expression.addPart(new Member());
			return true;
		}
		if (valueString.compareTo("value=\"range\"") == 0) {	//Range
			expression.addPart(new Range());
			return true;
		}
		if (valueString.compareTo("value=\"remove\"") == 0) {	//Remove
			expression.addPart(new Remove());
			return true;
		}
		if (valueString.compareTo("value=\"remove-all\"") == 0) {	//RemoveAll
			expression.addPart(new RemoveAll());
			return true;
		}
		if (valueString.compareTo("value=\"rest\"") == 0) {	//Rest
			expression.addPart(new Rest());
			return true;
		}
		if (valueString.compareTo("value=\"reverse\"") == 0) {	//Reverse
			expression.addPart(new Reverse());
			return true;
		}
		if (valueString.compareTo("value=\"second\"") == 0) {	//Second
			expression.addPart(new Second());
			return true;
		}
		if (valueString.compareTo("value=\"make-list\"") == 0) {	//Make-List
			expression.addPart(new MakeList());
			return true;
		}
		return false;
	}
}
