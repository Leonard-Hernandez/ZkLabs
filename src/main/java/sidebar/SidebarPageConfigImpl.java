package sidebar;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class SidebarPageConfigImpl implements SidebarPageConfig{
	
	HashMap<String, SidebarPage> pageMap = new LinkedHashMap<String, SidebarPage>();
	
	public SidebarPageConfigImpl() {
		pageMap.put("fn1", new SidebarPage("Arco", "Arco", "/img/site.png","https://github.com/Leonard-Hernandez/Archery-Api"));
		pageMap.put("fn2", new SidebarPage("Diana", "Diana", "/img/demo.png","https://github.com/Leonard-Hernandez/Java-Curso-Guzman"));
		pageMap.put("fn3", new SidebarPage("Notas", "Notas", "/img/doc.png", "https://github.com/Leonard-Hernandez/CarrosStockZk"));
	}
	
	@Override
	public List<SidebarPage> getPages(){
		return new ArrayList<SidebarPage>(pageMap.values());
	}
	
	@Override
	public SidebarPage getPage(String name){
		return pageMap.get(name);
	}
}
