
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;



/**
 *
 * @author ross
 */
public class PropertyServlet extends HttpServlet 
{
    private ServletContext context;
    private LinkedList<Property> properties;
    private LinkedList<Property> selectedProperties;
   
    @Override
    public void init()  //initializes servlet
    {
        context = getServletContext();  //used for logging
        properties = new LinkedList<Property>();
        context.log("Property List has been initialized.");
        selectedProperties= new LinkedList<Property>();//properties that match selection
        
        populateList();
    }
    
    public void populateList()  //generates sample properties
    {
        properties.add(new Property(1,1,50000,"Joe Bloggs","Sherry Fitz","Cork","Small Cottage in Country"));   //houses
        properties.add(new Property(1,2,100000,"John Doe","Douglas Newman","Dublin 5","Small Family Home"));
        properties.add(new Property(1,3,130000,"Joe Bloggs","Sherry Fitz","Galway","family Home"));
        properties.add(new Property(1,4,250000,"Mary Byrne","Douglas Newman","Mayo","Big Family Home"));
        properties.add(new Property(1,5,300000,"Mary Byrne","Douglas Newman","Dublin 9","Beatutiful Garden"));
        properties.add(new Property(1,6,400000,"Jane Sample","Sherry Fitz","Sligo","Mansion in Sligo"));
        
        properties.add(new Property(2,1,75000,"John Doe","Douglas Newman","Meath","Newly Constructed"));   //aparts
        properties.add(new Property(2,1,90000,"Jane Sample","Sherry Fitz","Louth","Ideal for commuters"));
        properties.add(new Property(2,2,150000,"Joe Bloggs","Sherry Fitz","Dublin 1","Very Central"));
        properties.add(new Property(2,2,175000,"John Doe","Douglas Newman","Dublin 2","Very Central"));
        properties.add(new Property(2,3,200000,"Mary Byrne","Douglas Newman","Cork","Great Location"));
        properties.add(new Property(2,3,300000,"Jane Sample","Sherry Fitz","Wicklow","Beatiful Apartment"));
        
        properties.add(new Property(3,0,100000,"Joe Bloggs","Sherry Fitz","Clare","1 Acre"));   //plots
        properties.add(new Property(3,0,150000,"Joe Bloggs","Sherry Fitz","Tipperary","1.5 Acres"));
        properties.add(new Property(3,0,300000,"Mary Byrne","Douglas Newman","Antrim","4 Acres"));
        properties.add(new Property(3,0,400000,"John Doe","Douglas Newman","Dublin 3","1 Acre"));
        properties.add(new Property(3,0,550000,"Jane Sample","Sherry Fitz","Cork","7 Acres"));
        properties.add(new Property(3,0,750000,"","Douglas Newman","Dublin 2","2 Acres"));
        
        properties.add(new Property(4,0,100000,"Jane Sample","Sherry Fitz","Clare","Corner Shop"));   //business
        properties.add(new Property(4,0,200000,"Mary Byrne","Douglas Newman","Galway","Pub"));
        properties.add(new Property(4,0,300000,"Joe Bloggs","Sherry Fitz","Armagh","Pharmacist"));
        properties.add(new Property(4,0,500000,"John Doe","Douglas Newman","Monaghan","Petrol Station"));
        properties.add(new Property(4,0,600000,"Jane Sample","Sherry Fitz","Dublin 4","Pub"));
        properties.add(new Property(4,0,750000,"John Doe","Douglas Newman","Dublin 2","Hotel"));
        
        context.log("24 sample properties added.");
    }
    public LinkedList getSelection(Selection s) //returns properties that match selection
    {
        LinkedList<Property> returnList = new LinkedList<Property>();
        for(Iterator it = properties.iterator();it.hasNext();)
        {
            Property p = (Property) it.next();
            if(p.isMatch(s))
            {
                returnList.add(p);
                
            }
        }
        context.log("Selection complete");
        return returnList;
    }
    
     protected void processRequest(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        res.setContentType("application/x-java-serialized-object");
            // If the client says "format=object" then

        if ("query".equals(req.getParameter("format")))     //for selections
        {
            InputStream in = req.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(in);
            Selection s = null;
            try {
                s = (Selection) ois.readObject();   //reads selection object
            } catch (ClassNotFoundException ex) {
                context.log("Class not Found Exception!");
            }
            if(s!=null)
            {
                selectedProperties = getSelection(s);   //calls get selection method
                OutputStream out = res.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(out);
                oos.writeObject(selectedProperties);    //returns the list of suitable properties
                context.log("Properties Returned");
                oos.flush();
                oos.close();
            }
            else
            {
                context.log("Selection s is null!");
            }
        }
         if ("add".equals(req.getParameter("format"))) //adds property to list
        {
            InputStream in = req.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(in);
            Property p = null;
            try {
                p = (Property) ois.readObject();    //read propertty
            } catch (ClassNotFoundException ex) {
                context.log("Class not Found Exception!");
            }
            if(p!=null) //adds it if not null
            {
                properties.add(p);
                context.log("Property added");
            }
            else
            {
                context.log("Property p is null.");
            }
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
        context.log("GET");
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
        context.log("POST");
    }
    
    /** Returns a short description of the servlet.
     */
}
