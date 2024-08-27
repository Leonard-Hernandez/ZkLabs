package todo;

import java.util.List;

import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

import entity.Priority;
import entity.Todo;
import services.TodoListService;

public class TodoListController extends SelectorComposer<Component> {

	@Wire
	Textbox todoSubject;
	@Wire
	Button addTodo;
	@Wire
	Listbox todoListBox;

	@Wire
	Component selectedTodoBlock;
	@Wire
	Checkbox selectedTodoCheck;
	@Wire
	Textbox selectedTodoSubject;
	@Wire
	Radiogroup selectedTodoPriority;
	@Wire
	Datebox selectedTodoDate;
	@Wire
	Textbox selectedTodoDescription;
	@Wire
	Button updateSelectedTodo;

	// service
	TodoListService todoListService = new TodoListServiceImpl();

	// datos para la vista
	ListModelList<Todo> todoListModel;
	ListModelList<Priority> priorityListModel;
	Todo selectedTodo;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		List<Todo> todoList = todoListService.getTodoList();
		todoListModel = new ListModelList<Todo>(todoList);
		todoListBox.setModel(todoListModel);

		priorityListModel = new ListModelList<Priority>(Priority.values());
		selectedTodoPriority.setModel(priorityListModel);

	}

	@Listen("onClick =#addTodo; onOK = #todoSubject")
	public void soTodoAdd() {
		// obteniendo el input del usuario
		String subject = todoSubject.getValue();
		if (Strings.isBlank(subject)) {
			Clients.showNotification("Nada por hacer ?", todoSubject);
		} else {
			// guardando datos
			selectedTodo = todoListService.saveTodo(new Todo(subject));
			// actualizando el modelo
			todoListModel.add(selectedTodo);
			// asignando la nueva seleccion
			todoListModel.addToSelection(selectedTodo);

			// refresh detail view
			refreshDetailView();

			// reiniciando el valor
			todoSubject.setValue("");
		}
	}
	
	@Listen("onClick = #updateSelectedTodo")
	public void doUpdateClick() {
		if(Strings.isBlank(selectedTodoSubject.getValue())) {
			Clients.showNotification("Nada por hacer ?", selectedTodoSubject);
			return;
		}		
		selectedTodo.setComplete(selectedTodoCheck.isChecked());
		selectedTodo.setSubject(selectedTodoSubject.getValue());
		selectedTodo.setDate(selectedTodoDate.getValue());
		selectedTodo.setDescription(selectedTodoDescription.getValue());
		selectedTodo.setPriority(priorityListModel.getSelection().iterator().next());
		
		selectedTodo = todoListService.updateTodo(selectedTodo);
		
		todoListModel.set(todoListModel.indexOf(selectedTodo), selectedTodo);
		
		Clients.showNotification("Todo guardado");
	}
	
	@Listen("onClick = #reloadSelectedTodo")
	public void doReloadClick() {
		refreshDetailView();
	}

	@Listen("onSelect = #todoListbox")
	public void doTodoSelect() {
		if (todoListModel.isSelectionEmpty()) {
			selectedTodo = null;
		}else {
			selectedTodo = todoListModel.getSelection().iterator().next();
		}
		refreshDetailView();
	}

	private void refreshDetailView() {
		// refresh the detail view of selected todo

		if (selectedTodo == null) {
			// clean
			selectedTodoBlock.setVisible(false);
			selectedTodoCheck.setChecked(false);
			selectedTodoSubject.setValue(null);
			selectedTodoDate.setValue(null);
			selectedTodoDescription.setValue(null);
			updateSelectedTodo.setDisabled(true);

			priorityListModel.clearSelection();
		} else {
			selectedTodoBlock.setVisible(true);
			selectedTodoCheck.setChecked(selectedTodo.isComplete());
			selectedTodoSubject.setValue(selectedTodo.getSubject());
			selectedTodoDate.setValue(selectedTodo.getDate());
			selectedTodoDescription.setValue(selectedTodo.getDescription());
			updateSelectedTodo.setDisabled(false);

			priorityListModel.addToSelection(selectedTodo.getPriority());

		}

	}

}
