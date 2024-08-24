package todo;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import entity.Priority;
import entity.Todo;
import services.TodoListService;

public class TodoListController extends SelectorComposer<Component>{
	
	@Wire
	Listbox todoListBox;
	
	
	//service
	TodoListService todoListService = new TodoListServiceImpl();
	
	//datos para la vista
	ListModelList<Todo> todoListModel;
	ListModelList<Priority> priorityListModel;
	Todo selectedTodo;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		List<Todo> todoList = todoListService.getTodoList();
		todoListModel = new ListModelList<Todo>(todoList);
		todoListBox.setModel(todoListModel);
		
	}
	
	
}
