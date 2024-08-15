package sidebar;

import java.util.List;

public interface SidebarPageConfig {
	
	//obtener todas las paginas
	public List<SidebarPage> getPages();
	
	//obtener una unica pagina
	public SidebarPage getPage (String name);

}
