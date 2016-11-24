package assignment05;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

/**
 * Servlet implementation class CartAdd
 */
@WebServlet(description = "add to cart", urlPatterns = { "/CartAdd" })
public class CartAdd extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CartAdd() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String item = request.getParameter("item");
		
		HttpSession session = request.getSession();
		ArrayList<String> list = new ArrayList<String>();
		
		if(session.getAttribute("itemList") != null){
			list = (ArrayList<String>) session.getAttribute("itemList");
		}
		list.add(item);
		session.setAttribute("itemList", list);
		
		//System.out.println(list);
		//System.out.println(list.size());
		
		response.getWriter().append("Total number of items in the cart: " + list.size());
		
		//response.getWriter().append("Served at shop: ").append(request.getContextPath());
	}

}
