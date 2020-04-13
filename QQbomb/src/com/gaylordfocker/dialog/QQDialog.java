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

	private String enemyName,message;//��ը�������ƣ���ը������
	
	public final int WIDTH = 300,HEIGHT = 30;
	
	private int times,cnt = 0,sequence,i;//��ը��������ǰ��ɵĺ�ը���������������ţ�Ԥ����ʱ���õļ�����
	
	private long period;//��ը���
	
	private JButton stopOrContinueBtn,resetBtn,removeBtn;
	
	private Icon stopIcon,continueIcon,resetIcon,removeIcon;
	
	private JLabel nameLb,msgLb,periodLb,timesLb;
	
	private Thread thread;
	
	/**
	 * 
	 * @param enemyName Ҫ��ը�Ķ���
	 * @param message Ҫ��ը����Ϣ,��Ϊnull��Ĭ��ѡ�������
	 * @param period Ҫ��ը��ʱ����
	 * @param times Ҫ��ը�Ĵ���
	 * @param sequence �˶Ի������
	 */
	public QQDialog(String enemyName,String message,long period,int times,int sequence){
		this.enemyName = enemyName;
		this.message = message;
		this.period = period;
		this.times = times;
		this.sequence = sequence;
		setBounds(0,HEIGHT*sequence,WIDTH,HEIGHT);
		initPanel();//��ʼ�����
		thread = new Thread(this);
	}
	
	@Override
	public void run() {
		try {
			
			for(i=5;i>0;i--) {
				timesLb.setText("Ԥ��:"+i);
				Thread.sleep(1000);
			}
			
			while(cnt<times) {
				cnt++;
				Thread.sleep(period);
				//���Ĳ��֣�����hz.exe��ɵ��κ�ը
				Process p;
				if(message != null){//�����ϢΪnull����ü�����
					if(!new File("config/hz.exe").exists()) {
							JOptionPane.showMessageDialog(null, "hz.exe������!�޷���ɺ�ը!", 
									"����", JOptionPane.ERROR_MESSAGE);
						}
					p = Runtime.getRuntime().exec("config/hz "+enemyName+" "+message);
				}else {
					if(!new File("hz_paste.exe").exists()) {
						JOptionPane.showMessageDialog(null, "hz_paste.exe������!�޷���ɺ�ը!", 
								"����", JOptionPane.ERROR_MESSAGE);
					}
					p = Runtime.getRuntime().exec("hz_paste "+enemyName);
				}
				
				if(p.waitFor()!=0) {//��ը�ɹ��򷵻�0�����ڲ������򷵻�1
					JOptionPane.showMessageDialog(null, "��ը����"+enemyName+"��������!", 
							"����", JOptionPane.ERROR_MESSAGE);
					stop();
				}
				
				timesLb.setText(cnt+"/"+times);
			}
			
			cnt = 0;
			timesLb.setText("��ɡ�");
			stopOrContinueBtn.setIcon(continueIcon);
			stopOrContinueBtn.setToolTipText("����");
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "�����쳣��","�쳣��",JOptionPane.ERROR_MESSAGE);
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
		
		msgLb = new JLabel(message!=null?message:" ������");
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
		stopOrContinueBtn.setToolTipText("����");
		stopOrContinueBtn.addActionListener((e)->{
			if(thread.isAlive()) {//�̴߳��Ļ�������ɱ��������ͼ�꣬�߳������Ļ����½��̲߳�����ͼ��
				stop();
			}else {
				stopOrContinueBtn.setIcon(stopIcon);
				stopOrContinueBtn.setToolTipText("��ͣ");
				cnt--;
				thread = new Thread(this);
				thread.start();
			}
		});
		add(stopOrContinueBtn);
		
		resetBtn = new JButton(resetIcon);
		resetBtn.setBounds(229, 2, 25, 25);
		resetBtn.setToolTipText("����");
		resetBtn.addActionListener((e)->{
			cnt = 0;
			i=5;
			stop();//����ʱ��ɱ���߳�
			timesLb.setText(cnt+"/"+times);
		});
		add(resetBtn);
		
		removeBtn = new JButton(removeIcon);
		removeBtn.setBounds(258, 2, 25, 25);
		removeBtn.setToolTipText("�Ƴ�");
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
			stopOrContinueBtn.setToolTipText("����");
			thread.stop();
//		}).start();
	}
	
	/**
	 * �˷������������Ƴ���ť����¼�����Ϊ��Ҫ��ȡ��������ܽ��Լ��Ƴ���
	 * ��Ϊ��Ҫ��������������µ�����������������¼���Ӧ���ڸ��������ɡ�
	 * @param al ������Ƴ������������¼�������
	 */
	public void setRemoveAction(ActionListener al) {
		
		removeBtn.addActionListener((e)->{
			stop();
			al.actionPerformed(e);
		});
		
	}
	
	/**
	 * ��ȡ��������ţ����ڸ��������������ϵ��������
	 * @return ���������
	 */
	public int getSequence() {
		return sequence;
	}
	
	/**
	 * ����������ơ�<br>
	 * sequence--;��ż�һ<br>
	 * setBounds(0,HEIGHT*sequence,WIDTH,HEIGHT);�������ȥһ����λ
	 */
	public void turnUp() {
		sequence--;
		setBounds(0,HEIGHT*sequence,WIDTH,HEIGHT);
	}
	
	/**
	 * ��д�����������ڸ�������ж��Ƿ����ظ���ը���������
	 * @return �����������к�ը������ͬ�򷵻�true
	 */
	@Override
	public boolean equals(Object o) {
		return (o instanceof QQDialog)&&
		this.enemyName.equals(((QQDialog)o).enemyName);
	}
	
}