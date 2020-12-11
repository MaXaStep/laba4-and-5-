package boostbrain;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.sql.rowset.spi.XmlReader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class App {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory spf = new SAXParserFactory.newInstance();
        SAXParser saxParser = new spf.newSAXParser();
        XmlReader xmlReader = new saxParser.getXMLReader();
        MyHandler handler = new MyHandler();
        xmlReader.setContentHandler(handler);
        xmlReader.parse(systemId: "D:\\java_projects\\sources\\employe.xml" );

        Report report = new handler.getReport();
        System.out.println("\nReport number " + report.number);
        report.employeeList.forEach(System.out::println);
    }


    private static class MyHandler extends DefaultHandler {
        static final String REPORT_TAG = "report";
        static final String EMPLOYERS_TAG = "employers";
        static final String EMPLOYE_TAG = "employe";
        static final String NAME_TAG = "name";
        static final String AGE_TAG = "age";
        static final String SALARY_TAG = "salary";
        static final String REPORT_NUMBER_ATTRIBUTE = "number";
        static final String EMPLOYERS_DEPARTMENT_ATTRIBUTE = "department";
        static final String EMPLOYE_NUMBER_ATTRIBUTE = "number";
        static final String CURRENCY_ATTRIBUTE = "currency";
        private Report report;
        private Employe currentEmploye;
        private String employersDepartment;
        private String currentElement;

        Report getReport() {
            return report;
        }

        public void startDocument() throws SAXException {
            System.out.println("Starting XML parsing...");
        }
        @Override
        public void startElement(String uri, String localName,String qName, Attributes attributes)throws  SAXException
        {
            switch (currentElement)
            {
                case REPORT_TAG:{
                    report=new Report();
                    report.number=Integer.valueOf(attributes.getValue(REPORT_NUMBER_ATTRIBUTE));

                }break;
                case EMPLOYERS_TAG:{
                    report.employeList=new ArrayList<>();
                    if(employersDepartment==null)
                    {
                        employersDepartment=attributes.getValue(EMPLOYERS_DEPARTMENT_ATTRIBUTE);
                    }
                }break;
                case EMPLOYE_TAG:{
                    currentEmploye=new Employe();
                    currentEmploye.number=Integer.valueOf(attributes.getValue(EMPLOYE_NUMBER_ATTRIBUTE));
                    currentEmploye.department=employersDepartment;

                }break;
                case SALARY_TAG:{
                    currentEmploye.salary= new Employe.Salary();
                    currentEmploye.salary.currency=attributes.getValue(CURRENCY_ATTRIBUTE);
                }break;

            }
        }


        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String text = new String(ch, start, length);
            if (text.contains("<") || currentElement == null) {
                return;
            }
            switch (currentElement) {
                case NAME_TAG: {
                    currentEmploye.name = text;
                }
                break;
                case AGE_TAG: {
                    currentEmploye.age = Integer.valueOf(text);
                }
                break;
                case SALARY_TAG: {
                    currentEmploye.salary.value = Double.valueOf(text);

                }
                break;
            }
        }

        @Override
        public void endElement(String uri,String localName,String qName)throws SAXException
        {
            switch (qName)
            {
                case EMPLOYE_TAG:{
                    report.employeList.add(currentEmploye);
                    currentEmploye=null;
                }break;

            }
                currentElement=null;
        }
            public void endDocument()throws SAXException
            {
                System.out.println("XML parsing is completed...");
            }


    }


}
class Report
{
    Integer number;
    List<Employe> employeList;
}
class Employe
{
    String department;
    Integer number;
    String name;
    Integer age;
    Salary salary;
    static class Salary
    {
        Double value;
        String currency;
    }

    @Override
    public String toString()
    {
        return "Employe ("+
                "department="+department+"\'"+
                "number="+number+"\'"+
                "name="+name+"\'"+
                "age="+age+"\'"+
                "salary="+salary.value+" "+salary.currency+")";

    }

}

