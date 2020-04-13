package com.gaylordfocker.dialog;

import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class QQDialog extends JPanel implements Runnable{

	private String enemyName,message;//轰炸对象名称，轰炸的内容
	
	public final int WIDTH = 300,HEIGHT = 30;
	
	private int times,cnt = 0,sequence,i;//轰炸次数，当前完成的轰炸次数，此任务的序号，预备的时候用的计数器
	
	private long period;//轰炸间隔
	
	private JButton stopOrContinueBtn,resetBtn,removeBtn;
	
	private Icon stopIcon,continueIcon,resetIcon,removeIcon;
	
	private JLabel nameLb,msgLb,periodLb,timesLb;
	
	private Thread thread;
	
	/**
	 * 
	 * @param enemyName 要轰炸的对象
	 * @param message 要轰炸的信息,若为null则默认选择剪贴板
	 * @param period 要轰炸的时间间隔
	 * @param times 要轰炸的次数
	 * @param sequence 此对话的序号
	 */
	public QQDialog(String enemyName,String message,long period,int times,int sequence){
		this.enemyName = enemyName;
		this.message = message;
		this.period = period;
		this.times = times;
		this.sequence = sequence;
		setBounds(0,HEIGHT*sequence,WIDTH,HEIGHT);
		initPanel();//初始化面板
		thread = new Thread(this);
	}
	
	@Override
	public void run() {
		try {
			
			for(i=5;i>0;i--) {
				timesLb.setText("预备:"+i);
				Thread.sleep(1000);
			}
			
			while(cnt<times) {
				cnt++;
				Thread.sleep(period);
				//核心部分：调用hz.exe完成单次轰炸
				Process p;
				if(message != null){//如果信息为null则调用剪贴板
					if(!new File("config/hz.exe").exists()) {
							JOptionPane.showMessageDialog(null, "hz.exe不存在!无法完成轰炸!", 
									"错误！", JOptionPane.ERROR_MESSAGE);
						}
					p = Runtime.getRuntime().exec("config/hz "+enemyName+" "+message);
				}else {
					if(!new File("hz_paste.exe").exists()) {
						JOptionPane.showMessageDialog(null, "hz_paste.exe不存在!无法完成轰炸!", 
								"错误！", JOptionPane.ERROR_MESSAGE);
					}
					p = Runtime.getRuntime().exec("hz_paste "+enemyName);
				}
				
				if(p.waitFor()!=0) {//轰炸成功则返回0，窗口不存在则返回1
					JOptionPane.showMessageDialog(null, "轰炸对象“"+enemyName+"”不存在!", 
							"错误！", JOptionPane.ERROR_MESSAGE);
					stop();
				}
				
				timesLb.setText(cnt+"/"+times);
			}
			
			cnt = 0;
			timesLb.setText("完成。");
			stopOrContinueBtn.setIcon(continueIcon);
			stopOrContinueBtn.setToolTipText("启动");
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "发生异常！","异常！",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	public void initPanel() {
		
		setLayout(null);
		setBorder(BorderFactory.createLoweredBevelBorder());
		
		nameLb = new JLabel(enemyName);
		nameLb.setBounds(0, 2, 50, 25);
		nameLb.setBorder(BorderFactory.createEtchedBorder());
		add(nameLb);
		
		msgLb = new JLabel(message!=null?message:" 剪贴板");
		msgLb.setBounds(50, 2, 50, 25);
		msgLb.setBorder(BorderFactory.createEtchedBorder());
		add(msgLb);
		
		periodLb = new JLabel(""+period+"ms");
		periodLb.setBounds(100, 2, 50, 25);
		periodLb.setBorder(BorderFactory.createEtchedBorder());
		add(periodLb);
		
		timesLb = new JLabel(+cnt+"/"+times);
		timesLb.setBounds(150, 2, 50, 25);
		timesLb.setBorder(BorderFactory.createEtchedBorder());
		add(timesLb);
		
		Image stopImage = new ImageIcon("config/stop.jpg").getImage().getScaledInstance(20, 20, Image.SCALE_FAST);
		stopIcon = new ImageIcon(stopImage);
		Image continueImage = new ImageIcon("config/continue.jpg").getImage().getScaledInstance(25, 25, Image.SCALE_FAST);
		continueIcon = new ImageIcon(continueImage);
		Image resetImage = new ImageIcon("config/reset.jpg").getImage().getScaledInstance(20, 20, Image.SCALE_FAST);
		resetIcon = new ImageIcon(resetImage);
		Image removeImage = new ImageIcon("config/remove.jpg").getImage().getScaledInstance(20, 20, Image.SCALE_FAST);
		removeIcon = new ImageIcon(removeImage);
		
		stopOrContinueBtn = new JButton(continueIcon);
		stopOrContinueBtn.setBounds(200, 2, 25, 25);
		stopOrContinueBtn.setToolTipText("启动");
		stopOrContinueBtn.addActionListener((e)->{
			if(thread.isAlive()) {//线程存活的话单机则杀死并更新图标，线程是死的话则新建线程并更新图标
				stop();
			}else {
				stopOrContinueBtn.setIcon(stopIcon);
				stopOrContinueBtn.setToolTipText("暂停");
				cnt--;
				thread = new Thread(this);
				thread.start();
			}
		});
		add(stopOrContinueBtn);
		
		resetBtn = new JButton(resetIcon);
		resetBtn.setBounds(229, 2, 25, 25);
		resetBtn.setToolTipText("重置");
		resetBtn.addActionListener((e)->{
			cnt = 0;
			i=5;
			stop();//重置时先杀死线程
			timesLb.setText(cnt+"/"+times);
		});
		add(resetBtn);
		
		removeBtn = new JButton(removeIcon);
		removeBtn.setBounds(258, 2, 25, 25);
		removeBtn.setToolTipText("移除");
		add(removeBtn);
		
	}
	
	@SuppressWarnings("deprecation")
	private void stop() {
//		new Thread(()->{
//			try {
//				Thread.sleep(200);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			stopOrContinueBtn.setIcon(continueIcon);
			stopOrContinueBtn.setToolTipText("启动");
			thread.stop();
//		}).start();
	}
	
	/**
	 * 此方法用来设置移除按钮点击事件，因为需要获取父组件才能将自己移除，
	 * 因为需要将此任务面板以下的任务面板上移所以事件响应需在父组件中完成。
	 * @param al 父组件移除此面板所需的事件监听器
	 */
	public void setRemoveAction(ActionListener al) {
		
		removeBtn.addActionListener((e)->{
			stop();
			al.actionPerformed(e);
		});
		
	}
	
	/**
	 * 获取此面板的序号，便于父组件将此序号以上的面板上移
	 * @return 此面板的序号
	 */
	public int getSequence() {
		return sequence;
	}
	
	/**
	 * 将此面板上移。<br>
	 * sequence--;序号减一<br>
	 * setBounds(0,HEIGHT*sequence,WIDTH,HEIGHT);纵坐标减去一个单位
	 */
	public void turnUp() {
		sequence--;
		setBounds(0,HEIGHT*sequence,WIDTH,HEIGHT);
	}
	
	/**
	 * 重写方法，方便在父面板中判断是否有重复轰炸对象的任务
	 * @return 如果两个面板中轰炸对象相同则返回true
	 */
	@Override
	public boolean equals(Object o) {
		return (o instanceof QQDialog)&&
		this.enemyName.equals(((QQDialog)o).enemyName);
	}
	
}