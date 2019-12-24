import java.util.ArrayList;

import beans.Arc;
import beans.Task;

/**
 * 该类获得当前可执行的任务集的作用。
 * @author 65635
 */
public class Start {

	/**
	 * 任务集
	 */
	private ArrayList<Task> tasks;
	
	/**
	 * 弧集
	 */
	private ArrayList<Arc> arcs;
	
	/**
	 * 当前已完成任务集
	 */
	private ArrayList<Task> finished_j;
	
	/**
	 * 全部任务集
	 */
	private int[] all_t;
	
	/**
	 * 当前可执行的任务集
	 */
	private ArrayList<Integer> startNumber;

	/**
	 * 该方法获得当前可执行的任务集startNumber。
	 * @param tasks_c 任务集
	 * @param arcs_c 弧集
	 * @param finished_j_c 已完成任务集
	 * @return 返回当前可执行任务集ArrayList<Integer>
	 */
	public ArrayList<Integer> getStartNumbers(ArrayList<Task> tasks_c, ArrayList<Arc> arcs_c, 
			ArrayList<Task> finished_j_c){
		
		this.tasks = tasks_c;
		this.arcs = arcs_c;
		this.finished_j = finished_j_c;
		
		//记录所有弧的起点（可重复）
		int[] firstNumbers = new int[arcs.size()];
		//记录所有弧的终点（可重复）
		int[] lastNumbers = new int[arcs.size()];
		//可执行任务集
		startNumber = new ArrayList<Integer>();	
		
		//初始化全部任务集
		all_t = new int[tasks.size()];
		for (int i = 0; i < tasks.size(); i++) {
			all_t[i] = i;
		}
		//获取全部起点
		for (int i = 0; i < arcs.size(); i++) {
			firstNumbers[i] = arcs.get(i).getFirstNumber();
		}
		//获取全部终点
		for (int i = 0; i < arcs.size(); i++) {
			lastNumbers[i] = arcs.get(i).getLastNumber();
		}
		
		//找到不是终点的起点，该弧对应的起点是当前可执行的出发点
		for (int i = 0; i < firstNumbers.length; i++) {
			for (int j = 0; j < lastNumbers.length; j++) {
				if (firstNumbers[i]==lastNumbers[j]) {
					break;
				}else if (j<(lastNumbers.length-1)) {
					continue;
				}else {
					Integer start = firstNumbers[i];
					if(!startNumber.contains(start)){
						startNumber.add(start);
					}
				}
			}
		}
		
		//找到TPG图中孤立的任务
		for (int a = 0; a < all_t.length; a++) {
			//去除已完成任务
			for (int f = 0; f < finished_j.size(); f++) {
				if (a == finished_j.get(f).getNumber()) {
					all_t[a] = -1;
				}
			}
			//去除弧中现有的任务（未完成任务）
			for (int f = 0; f < firstNumbers.length; f++) {
				if (a == firstNumbers[f]) {
					all_t[a] = -1;
				}
			}
			//去除拥有前项弧的任务
			for (int l = 0; l < lastNumbers.length; l++) {
				if (a == lastNumbers[l]) {
					all_t[a] = -1;
				}
			}
		}
		//当前all_t中值不为-1对于的位置数a即为孤立的任务
		//孤立的任务为当前可执行任务，因为没有任务约束
		for (int a = 0; a < all_t.length; a++) {
			if (all_t[a] != -1) {
				Integer start = all_t[a];
				if(!startNumber.contains(start)){
					startNumber.add(start);
				}
			}
		}
		
		return startNumber;
	}
}
