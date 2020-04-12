package com.gaylordfocker.dialog;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.gaylordfocker.Main.MainFrame;

@SuppressWarnings("serial")
public class SettingDialogFactory extends JDialog{

	public final int WIDTH = 200,HEIGHT = 250;
	
	private int sequence;
	
	private JButton okBtn;
	
	private JTextField enemyNameTf,messageTf,timesTf,periodTf;
	
	private JLabel enemyNameLb,messageLb,periodLb,timesLb;
	
	private ButtonGroup bg;
	
	private JRadioButton pasteBtn,customBtn;
	
	private QQDialog qqdialog;
	
	public SettingDialogFactory(MainFrame mf) {
		super(mf,"新建",true);
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setLayout(null);
		initPane();
	}
	
	public void initPane() {
		okBtn = new JButton("确定");
		okBtn.setBounds(64, 170, 60, 30);
		okBtn.addActionListener((e)->{
			try {

				if(enemyNameTf.getText().equals("")) {
					JOptionPane.showMessageDialog(this, "轰炸对象不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(customBtn.isSelected()&&messageTf.getText().equals("")) {
					JOptionPane.showMessageDialog(this, "轰炸信息不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
					return;
				}
				long period = Long.parseLong(periodTf.getText());
				int times = Integer.parseInt(timesTf.getText());
				if(times<=0) {
					JOptionPane.showMessageDialog(this, "轰炸次数应大于零！", "错误", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(period<=0) {
					JOptionPane.showMessageDialog(this, "轰炸间隔应大于等于零！", "错误", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				qqdialog = new QQDialog(enemyNameTf.getText(), customBtn.isSelected()
					?messageTf.getText():null, period, times, sequence);
				setVisible(false);
			}catch(Exception ex){
				JOptionPane.showMessageDialog(this, "请输入数字！", "错误", JOptionPane.ERROR_MESSAGE);
			}
			
		});
		add(okBtn);
		
		JPanel enemynamePn = new JPanel(null);
		enemynamePn.setBounds(0, 0, WIDTH, 30);
		enemynamePn.setBorder(BorderFactory.createEtchedBorder());
		add(enemynamePn);
		
		enemyNameLb = new JLabel("   轰炸对象:");
		enemyNameLb.setBounds(0, 0, 80, 30);
		enemynamePn.add(enemyNameLb);
		
		enemyNameTf = new JTextField();
		enemyNameTf.setBounds(80, 5, 100, 20);
		enemynamePn.add(enemyNameTf);
		
		JPanel messagePn = new JPanel(null);
		messagePn.setBounds(0, 30, WIDTH, 60);
		messagePn.setBorder(BorderFactory.createEtchedBorder());
		add(messagePn);
		
		messageLb = new JLabel("   轰炸内容:");
		messageLb.setBounds(0, 15, 80, 30);
		messagePn.add(messageLb);
		
		bg = new ButtonGroup();
		pasteBtn = new JRadioButton("剪贴板");
		pasteBtn.setBounds(70, 5, 80, 20);
		pasteBtn.setSelected(true);
		messagePn.add(pasteBtn);
		bg.add(pasteBtn);
		
		customBtn = new JRadioButton();
		customBtn.setBounds(70, 30, 20, 20);
		messagePn.add(customBtn);
		bg.add(customBtn);
		
		messageTf = new JTextField();
		messageTf.setBounds(90, 30, 90, 20);
		messagePn.add(messageTf);
		
		JPanel periodPn = new JPanel(null);
		periodPn.setBounds(0, 90, WIDTH, 30);
		periodPn.setBorder(BorderFactory.createEtchedBorder());
		add(periodPn);
		
		periodLb = new JLabel("   轰炸间隔:");
		periodLb.setBounds(0, 0, 80, 30);
		periodPn.add(periodLb);
		
		periodTf = new JTextField();
		periodTf.setBounds(80, 5, 100, 20);
		periodPn.add(periodTf);
		
		JPanel timesPn = new JPanel(null);
		timesPn.setBounds(0, 120, WIDTH, 30);
		timesPn.setBorder(BorderFactory.createEtchedBorder());
		add(timesPn);
		
		timesLb = new JLabel("   轰炸次数:");
		timesLb.setBounds(0, 0, 80, 30);
		timesPn.add(timesLb);
		
		timesTf = new JTextField();
		timesTf.setBounds(80, 5, 100, 20);
		timesPn.add(timesTf);
		
	}
	/**
	 * 
	 * @param sequence 构造出的QQDialog的序号
	 * @return 由对话框填写的内容构造的QQDialog对象，如果对话框被关闭则返回null。
	 */
	public QQDialog createSettingDialog(int sequence) {
		this.sequence = sequence;
		setVisible(true);
		return qqdialog;
	}
	
	public static void main(String[] args) {
		new SettingDialogFactory(null).setVisible(true);
	}

}