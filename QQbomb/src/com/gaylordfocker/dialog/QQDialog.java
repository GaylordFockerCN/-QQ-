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

	private String enemyName,message;
	
	public final int WIDTH = 300,HEIGHT = 30;
	
	private int times,cnt = 0,sequence,i;
	
	private long period;
	
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
		initPanel();
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
				
				Process p;
				if(message != null){
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
				
				if(p.waitFor()!=0) {
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
			if(thread.isAlive()) {
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
			stop();
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
	
	public void setRemoveAction(ActionListener al) {
		
		removeBtn.addActionListener((e)->{
			stop();
			al.actionPerformed(e);
		});
	}
	
	public int getSequence() {
		return sequence;
	}
	
	public void turnUp() {
		sequence--;
		setBounds(0,HEIGHT*sequence,WIDTH,HEIGHT);
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof QQDialog)&&
		this.enemyName.equals(((QQDialog)o).enemyName);
	}
	
}