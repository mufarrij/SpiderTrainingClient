package se.cambiosys.client.trainingclient.views;

import se.cambio.platform.cc.hcm.contact.dto.ContactDTO;
import se.cambio.platform.cc.hcm.contact.dto.impl.ContactDTOImpl;
import se.cambiosys.client.framework.DefaultExceptionHandler;
import se.cambiosys.client.framework.Framework;
import se.cambiosys.client.framework.components.CambioButton;
import se.cambiosys.client.framework.components.CambioComboBox;
import se.cambiosys.client.framework.components.CambioDateChooser;
import se.cambiosys.client.framework.components.CambioInternalFrame;
import se.cambiosys.client.framework.components.CambioLabel;
import se.cambiosys.client.framework.components.CambioList;
import se.cambiosys.client.framework.components.CambioPanel;
import se.cambiosys.client.framework.components.CambioScrollPane;
import se.cambiosys.client.framework.components.CambioSelectionComboBox;
import se.cambiosys.client.framework.components.CambioTable;
import se.cambiosys.client.framework.components.CambioTextArea;
import se.cambiosys.client.framework.components.CambioTimeChooser;
import se.cambiosys.client.framework.settings.SettingHandler;
import se.cambiosys.client.framework.settings.SettingHandlerService;
import se.cambiosys.client.framework.subjectofcare.SubjectOfCareToolkit;
import se.cambiosys.client.framework.subjectofcare.SubjectOfCareWrapper;
import se.cambiosys.client.framework.units.gui.UnitSelectionComponent;
import se.cambiosys.client.healthcaremodel.HCMSettingHandler;
import se.cambiosys.client.healthcaremodel.contact.ContactDataProvider;
import se.cambiosys.client.healthcaremodel.contact.gui.ContactStatusSelectionEditor;
import se.cambiosys.client.infoclass.ActionWrapper;
import se.cambiosys.client.resourceplanning.component.CareProviderSelectionComboBox;
import se.cambiosys.client.trainingclient.models.MedicalRecord;
import se.cambiosys.client.trainingclient.models.MedicalRecordModel;
import se.cambiosys.client.healthcaremodel.contact.gui.ContactSelectionComponent;
import se.cambiosys.client.trainingclient.settings.SettingFacade;
import se.cambiosys.spider.DataService.MultiValuedData;
import se.cambiosys.spider.HealthCareModel.ContactData;
import se.cambiosys.spider.HealthCareModel.ContactFilter;
import se.cambiosys.spider.PatientService.PatientData;
import se.cambio.platform.cc.hcm.contact.Contact;


import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListDataListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordView  extends CambioInternalFrame
{

  private static MedicalRecordView medicalRecordView = null;

  private ContactSelectionComponent contactSelectionComponent;
  private CambioSelectionComboBox unitSelection;
  private ContactData[] currentContactData;
  private DefaultMutableTreeNode root = new DefaultMutableTreeNode("AllContacts");


  private MedicalRecordView()
  {
    try
    {
      setSOCRelated(new String[] { SubjectOfCareToolkit.PATIENT });
      String currentUserId = Framework.getInstance().getCurrentUserId();
      this.setTitle(currentUserId);
      initGUI();
    }
    catch(Exception e)
    {
      DefaultExceptionHandler.getInstance().handleThrowable(e);
    }
  }

  public static MedicalRecordView getInstance()
  {
    if(medicalRecordView == null)
    {
      CambioPanel cambioPanel;
      medicalRecordView = new MedicalRecordView();
    }
    return medicalRecordView;
  }

  @Override
  public void setActiveSOC(SubjectOfCareWrapper soc) throws Exception
  {
    if(Framework.getInstance().getActiveSubjectOfCare().id!=null)
    {
      loadData();
    }
    else
    {
      clearContactData();
    }
    super.showWindowContent();
  }

  @Override
  public void updateActiveSOCData(SubjectOfCareWrapper soc)
  {
    super.showWindowContent();
  }


  public CambioPanel createUnitPanel()
  {
     CambioPanel unitPanel = new CambioPanel();
     CambioLabel unitLabel = new CambioLabel(" Unit:");
     //unitLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

     String[] units = { "unit1", "unit2", "unit3", "unit4", "unit5" };
     List<String> unitList = new ArrayList();
     unitList.add("unit1");
     unitList.add("unit2");
     CambioComboBox unitComboBox = new CambioComboBox(unitList.toArray());
     unitSelection = new CambioSelectionComboBox(SettingHandlerService.UNIT_SELECTION_FOR_WORKING_UNIT);
     unitSelection.loadSelection();
     unitSelection.setPreferredSize(new Dimension(150,20));
     UnitSelectionComponent unitSelectionComponent = new UnitSelectionComponent();

     root = new DefaultMutableTreeNode("AllContacts");
     DefaultMutableTreeNode sub1= new DefaultMutableTreeNode("sub1");
     DefaultMutableTreeNode sub2 = new DefaultMutableTreeNode("sub2");
     DefaultMutableTreeNode sub3 = new DefaultMutableTreeNode("sub2");

     root.add(sub1);
     root.add(sub2);
     root.add(sub3);

     JTree tree = new JTree(root);

     CambioScrollPane unitScrollPane = new CambioScrollPane(tree);
     unitScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
     unitScrollPane.setSize(new Dimension(150,650));

     unitPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
     unitPanel.setLayout(new GridBagLayout());
     //unitPanel.setPreferredSize(new Dimension(200,800));

     GridBagConstraints gbc = new GridBagConstraints();
     gbc.fill = GridBagConstraints.HORIZONTAL;
     gbc.anchor = GridBagConstraints.NORTHWEST;
     gbc.weightx = 1;
     gbc.weighty = 0;
     gbc.insets = new Insets(2, 2, 0, 2);

     int i = 0;

     gbc.gridx = 0;
     gbc.gridy = i;
     gbc.weightx = 0;
     gbc.gridwidth = 1;
     unitPanel.add(unitLabel, gbc);

     gbc.gridx = 1;
     gbc.gridy = i;
     gbc.weightx = 0;
     gbc.gridwidth = 1;
     gbc.fill = GridBagConstraints.HORIZONTAL;
     unitPanel.add(unitSelection, gbc);

     i++;

     gbc.gridx = 0;
     gbc.gridy = i;
     gbc.weightx = 1;
     gbc.weighty = 1;
     gbc.gridwidth = 2;
     gbc.anchor = GridBagConstraints.NORTH;
     gbc.fill = GridBagConstraints.BOTH;
     unitPanel.add(unitScrollPane, gbc);

     unitSelection.addActionListener(new ActionListener()
     {
       @Override public void actionPerformed(ActionEvent e)
       {
         JFrame f = new JFrame();
         JOptionPane.showMessageDialog(f, unitSelection.getSelectedId());
         ContactData data = contactSelectionComponent.getSelectedContactData();
         contactSelectionComponent.setSelectedCareUnit(unitSelection.getSelectedId());

         try
         {
           String[] settingPath =
             new String[] { "ContactStatusSetting", "AllowNewContactChoice", "UseSelectionForContactSearch",
                            "SuggestContact", "UseInfoClass", "MandatoryConnectionToCareContact" };
           MultiValuedData[] filterdata =
             SettingHandler.getInstance().getSettingValues(HCMSettingHandler.PARENT_PATH, settingPath);
           String statusValue = filterdata[0].stringValue;
           ContactFilter m_filter = ContactStatusSelectionEditor.getContactFilter(statusValue);

           String[] perfUnitValues = null;
           String[] respUnitValues = null;

           currentContactData = ContactDataProvider.readContacts(Framework.getInstance().getActiveSubjectOfCare().id, m_filter, perfUnitValues, respUnitValues);
           List<ContactData> selectedContacts = new ArrayList<ContactData>();

           for(int i = 0 ; i < currentContactData.length ; i++)
           {
             if(currentContactData[i].staffed.performingUnit.equals(unitSelection.getSelectedId()))
             {
               selectedContacts.add((ContactData)currentContactData[i]);
             }
           }

           root.removeAllChildren();

           for(int i = 0 ; i < selectedContacts.size() ; i++)
           {
             String contactAsStringDtl = ContactDataProvider.getStringForContact(selectedContacts.get(i), HCMSettingHandler.getContactPresentationSettingValue());
             DefaultMutableTreeNode node = new DefaultMutableTreeNode(contactAsStringDtl);
             root.add(node);
           }

           ((DefaultTreeModel)tree.getModel()).reload();

         }
         catch (Exception ex){
           DefaultExceptionHandler.getInstance().handleThrowable(ex);
         }
       }

     });

     return unitPanel;

  }


  public CambioPanel createTablePanel()
  {
    MedicalRecord record = new MedicalRecord("2019-06-13 12:10:10","example","sc","sample","id_contact");
    List<MedicalRecord> recordList = new ArrayList<>();
    recordList.add(record);
    recordList.add(record);
    recordList.add(record);
    MedicalRecordModel medicalRecordModel = new MedicalRecordModel(recordList);

    CambioTable medicalRecordTable = new CambioTable();
    medicalRecordTable.setModel(medicalRecordModel);

    //reading column order setting for the patient table
    MultiValuedData columnOrder = SettingFacade.getInstance().getColumnOrderSetting();
    medicalRecordTable.customizeColumnModel(columnOrder);

    CambioScrollPane medicalRecordTablePane = new CambioScrollPane(medicalRecordTable);
    CambioPanel medicalRecordPanel = new CambioPanel();
    medicalRecordPanel.setLayout(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(5, 5, 5, 5);

    int i=0;

    gbc.gridx = 0;
    gbc.gridy = i;
    gbc.gridwidth = 4;
    medicalRecordPanel.add(medicalRecordTablePane, gbc);

    return medicalRecordPanel;
  }


  public CambioPanel createTablePanelNew()
  {
    MedicalRecord record = new MedicalRecord("2019-06-13 12:10:10","example","sc","sample","id_contact");
    List<MedicalRecord> recordList = new ArrayList<MedicalRecord>();
    recordList.add(record);
    recordList.add(record);
    recordList.add(record);
    MedicalRecordModel medicalRecordModel = new MedicalRecordModel(recordList);

    CambioTable userTable = new CambioTable();
    userTable.setModel(medicalRecordModel);

    //reading column order setting for the patient table
    MultiValuedData columnOrder = SettingFacade.getInstance().getColumnOrderSetting();
    userTable.customizeColumnModel(columnOrder);

    CambioScrollPane medicalRecordTablePane = new CambioScrollPane(userTable);
    CambioPanel medicalRecordPanel = new CambioPanel();
    medicalRecordPanel.setLayout(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(5, 5, 5, 5);

    int i=0;

    gbc.gridx = 0;
    gbc.gridy = i;
    gbc.gridwidth = 4;
    medicalRecordPanel.add(medicalRecordTablePane, gbc);

    return medicalRecordPanel;
  }


  public CambioPanel createNewButtonPanel()
  {
    CambioPanel newButtonPanel = new CambioPanel();
    newButtonPanel.setLayout(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(5, 5, 5, 5);

    int i = 0;

    gbc.gridx = i;
    gbc.gridy = 0;


    newButtonPanel.add(new CambioLabel("   "),gbc);

    i++;
    gbc.gridx = i;
    gbc.gridwidth=1;
    newButtonPanel.add(new CambioLabel("   "),gbc);

    i++;
    gbc.gridx = i;
    gbc.gridwidth=1;
    newButtonPanel.add(new CambioLabel("   "),gbc);

    i++;
    gbc.gridx = i;
    gbc.gridwidth=1;
    newButtonPanel.add(new CambioLabel("   "),gbc);

    i++;
    gbc.gridx = i;
    gbc.gridwidth=1;
    newButtonPanel.add(new CambioLabel("   "),gbc);

    i++;
    gbc.gridx = i;
    gbc.gridwidth=1;
    newButtonPanel.add(new CambioLabel("   "),gbc);

    i++;
    gbc.gridx = i;
    gbc.gridwidth=1;
    newButtonPanel.add(new CambioLabel("   "),gbc);

    i++;
    gbc.gridx = i;
    gbc.gridwidth=1;
    newButtonPanel.add(new CambioLabel("   "),gbc);


    CambioButton  newButton = new CambioButton("New");

    i++;
    gbc.gridx=i;
    newButtonPanel.add(newButton,gbc);

    return newButtonPanel;
  }

  public CambioPanel createButtonPanel()
  {
    CambioPanel buttonPanel = new CambioPanel();
    buttonPanel.setLayout(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(5, 5, 5, 5);

    int i = 0;

    gbc.gridx = i;
    gbc.gridy = 0;
    gbc.gridwidth=1;

    buttonPanel.add(new CambioLabel("   "),gbc);

    i++;

    gbc.gridx = i;

    buttonPanel.add(new CambioLabel("   "),gbc);

    i++;

    gbc.gridx = i;

    buttonPanel.add(new CambioLabel("   "),gbc);

    i++;

    gbc.gridx = i;

    buttonPanel.add(new CambioLabel("   "),gbc);

    CambioButton  removeButton = new CambioButton("Remove");
    CambioButton  saveButton = new CambioButton("Save");
    CambioButton  signButton = new CambioButton("Sign");
    CambioButton  closeButton = new CambioButton("Close");

    i++;

    gbc.gridx = i;

    buttonPanel.add(removeButton,gbc);

    i++;

    gbc.gridx = i;

    buttonPanel.add(saveButton,gbc);

    i++;

    gbc.gridx = i;

    i++;

    buttonPanel.add(signButton,gbc);

    i++;

    gbc.gridx = i;

    buttonPanel.add(closeButton,gbc);


    return buttonPanel;

  }

  public  CambioPanel createnotePicker()
  {
    CambioPanel notePicker = new CambioPanel();
    notePicker.setLayout(new GridBagLayout());
    notePicker.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    CambioLabel noteType = new CambioLabel("Note Type");
    noteType.setSize(new Dimension(400,50));
    CambioLabel careProvider = new CambioLabel("Care Provider");
    careProvider.setSize(new Dimension(400,50));
    CambioLabel unit = new CambioLabel("Unit");
    unit.setSize(new Dimension(400,50));
    CambioLabel dateTime = new CambioLabel("Date/time");
    dateTime.setSize(new Dimension(400,50));

    String[] units = { "unit1", "unit2", "unit3", "unit4", "unit5" };
    CambioComboBox unitComboBox = new CambioComboBox(units);

    CambioSelectionComboBox unitSelection = new CambioSelectionComboBox(SettingHandlerService.UNIT_SELECTION_FOR_WORKING_UNIT);
    unitSelection.loadSelection();
    unitSelection.setPreferredSize(new Dimension(150,20));

    String[] careProviders = { "DR.Nimal", "Anna", "Nurse"};
    CambioComboBox careProviderComboBox = new CambioComboBox(careProviders);
    CareProviderSelectionComboBox careProviderSelectionComboBox = new CareProviderSelectionComboBox();
    String[] noteTypes = {"Admisssion","Anestetic","Surgery",""};
    CambioComboBox noteTypesComboBox = new CambioComboBox(noteTypes);

    CambioDateChooser datePicker1 = new CambioDateChooser();
    CambioTimeChooser timePicker = new CambioTimeChooser();

    GridBagConstraints gbc = new GridBagConstraints();

    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(2,2,2,2);

    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.gridx = 0;
    gbc.gridy = 0;

    notePicker.add( noteType , gbc);

    gbc.gridwidth = 2;
    gbc.gridx = 1;
    gbc.gridy = 0;

    notePicker.add(noteTypesComboBox , gbc);

    gbc.gridwidth = 1;
    gbc.gridx = 0;
    gbc.gridy = 1;

    notePicker.add(careProvider ,gbc );

    gbc.gridwidth = 2;
    gbc.gridx = 1;
    gbc.gridy = 1;

    notePicker.add(careProviderSelectionComboBox ,gbc);

    gbc.gridwidth = 1;
    gbc.gridx = 0;
    gbc.gridy = 2;

    notePicker.add(unit,gbc);

    gbc.gridwidth = 2;
    gbc.gridx = 1;
    gbc.gridy = 2;

    notePicker.add(unitSelection,gbc);

    gbc.gridwidth = 1;
    gbc.gridx = 0;
    gbc.gridy = 3;

    notePicker.add(dateTime , gbc);

    gbc.gridwidth = 1;
    gbc.gridx = 1;
    gbc.gridy = 3;

    notePicker.add(datePicker1 ,gbc);

    gbc.gridwidth = 1;
    gbc.gridx = 2;
    gbc.gridy = 3;

    notePicker.add(timePicker , gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.ipady = 20;

    notePicker.add(new CambioLabel(""),gbc);


    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.gridwidth = 1;

    notePicker.add(new CambioLabel(""),gbc);


    gbc.gridx = 1;
    gbc.gridwidth = 1;

    CambioLabel unsigned = new CambioLabel("Unsigned");
    unsigned.setForeground(Color.RED);

    notePicker.add(unsigned,gbc);

    gbc.gridx = 2;

    notePicker.add(new CambioLabel(),gbc);


    return notePicker;
  }

  public CambioPanel createNotePanel()
  {
    CambioPanel notePanel = new CambioPanel(new GridBagLayout());
    notePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.anchor = GridBagConstraints.LINE_START;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets(1, 1, 1, 1);

    int i = 0;

    c.gridx = 0;
    c.gridy = i;
    c.gridwidth = 2;

    //combo
    String[] contact = { "c1", "c2", "c3", "c4", "c5" };
    CambioComboBox contactComboBox = new CambioComboBox(contact);

    contactSelectionComponent = new ContactSelectionComponent();
    contactSelectionComponent.setShowOnlyContactCombo(true);

    notePanel.add(contactSelectionComponent,c);

    i++;

    CambioPanel notePicker = createnotePicker();

    c.gridx = 0;
    c.gridy = i;
    c.gridwidth = 1;
    c.weightx = 0;

    notePanel.add(notePicker,c);

    CambioTextArea expan = new CambioTextArea();
    expan.setText("test text i am");
    CambioScrollPane panscr= new CambioScrollPane(expan);

    c.gridx = 1;
    c.gridy = i;
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0 ;

    notePanel.add( panscr, c );

    CambioPanel buttonPanel = createButtonPanel();

    i++;

    c.gridx = 0;
    c.gridy = i;
    c.gridwidth = 2;

    notePanel.add( buttonPanel , c );


    contactSelectionComponent.addActionListener(new ActionListener()
    {
      @Override public void actionPerformed(ActionEvent e)
      {
         ContactData contactData = contactSelectionComponent.getSelectedContactData();
      }

    });

    return notePanel;
  }

  public void initGUI()
  {
    CambioPanel mainPanel = new CambioPanel(new GridBagLayout());
    this.getContentPane().add(mainPanel);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(2, 2, 2, 2);

    int i = 0;

    gbc.gridx = 0;
    gbc.gridy = i;
    gbc.weightx = 0;
    gbc.gridwidth = 1;

    CambioPanel unitPanel = this.createUnitPanel();
    unitPanel.setPreferredSize(new Dimension(200,800));
    mainPanel.add(unitPanel,gbc);


    CambioPanel tablePanel = createTablePanel();
    tablePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    tablePanel.setPreferredSize(new Dimension(800,215));

    gbc.gridx =1;
    gbc.weightx = 1;
    gbc.gridwidth = 2;

    CambioPanel newButtonPanel = createNewButtonPanel();
    //newButtonPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    newButtonPanel.setPreferredSize(new Dimension(800,35));

    CambioPanel notePanel = createnotePicker();
    notePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    notePanel.setPreferredSize(new Dimension(800,350));

    CambioPanel tableNotePanel = new CambioPanel(new GridBagLayout());
    tableNotePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.LINE_START;
    c.weightx = 1;
    c.weighty = 1;
    c.insets = new Insets(2, 2, 2, 2);

    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 1;
    c.gridwidth = 2;

    tableNotePanel.add(tablePanel,c);

    c.gridy = 1;

    tableNotePanel.add(newButtonPanel , c);

    c.gridy = 2;

    CambioPanel notePanel1 = createNotePanel();

    tableNotePanel.add(notePanel1,c);


       /* c.gridy = 2;

        //combo
        String[] contact = { "c1", "c2", "c3", "c4", "c5" };
        JComboBox contactComboBox = new JComboBox(contact);

        tableNotePanel.add(contactComboBox,c);

        c.gridy = 3;
        c.gridwidth = 1;

        tableNotePanel.add(notePanel,c);

        JTextArea expan = new JTextArea();
        expan.setText("test text i am");
        JScrollPane panscr= new JScrollPane(expan);

        c.gridx = 1;
        c.fill = GridBagConstraints.BOTH;
        tableNotePanel.add(panscr,c);*/

    mainPanel.add(tableNotePanel,gbc);

    this.setSize(800,800);
    this.setResizable(false);
    this.pack();
    this.setVisible(true);

  }





  /*public void initGUI()
  {
    CambioPanel mainPanel = new CambioPanel(new GridBagLayout());
    this.getContentPane().add(mainPanel);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(2, 2, 2, 2);

    int i = 0;

    gbc.gridx = 0;
    gbc.gridy = i;
    gbc.weightx = 0.0;
    gbc.gridwidth = 1;

    CambioPanel unitPanel = this.createUnitPanel();
    mainPanel.add(unitPanel,gbc);


    CambioPanel tablePanel = createTablePanel();
    tablePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    tablePanel.setPreferredSize(new Dimension(800,210));

    gbc.gridx =1;
    gbc.weightx = 1;

    CambioPanel newButtonPanel = createNewButtonPanel();
    //newButtonPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    newButtonPanel.setPreferredSize(new Dimension(800,40));

    CambioPanel notePanel = new CambioPanel();
    notePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    notePanel.setPreferredSize(new Dimension(800,350));

    CambioPanel tableNotePanel = new CambioPanel(new GridBagLayout());
    tableNotePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.NORTHWEST;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets(2, 2, 2, 2);

    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 0.0;
    c.gridwidth = 1;

    tableNotePanel.add(tablePanel,c);

    c.gridy = 1;

    tableNotePanel.add(newButtonPanel , c);

    c.gridy = 2;

    tableNotePanel.add(notePanel,c);

    mainPanel.add(tableNotePanel,gbc);

    this.setSize(800,800);
    this.setResizable(false);
    this.pack();
    this.setVisible(true);

  }

  public CambioPanel createNewButtonPanel()
  {
    CambioPanel newButtonPanel = new CambioPanel();
    newButtonPanel.setLayout(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(5, 5, 5, 5);

    gbc.gridx = 0;
    gbc.gridy = 0;
    CambioLabel  empLabel = new CambioLabel();

    newButtonPanel.add(empLabel,gbc);

    CambioLabel  empLabel1 = new CambioLabel();

    gbc.gridx = 1;
    gbc.gridwidth=1;
    newButtonPanel.add(empLabel1,gbc);

    CambioLabel empLabel2 = new CambioLabel();
    gbc.gridx =2;
    newButtonPanel.add(empLabel2,gbc);


    CambioLabel empLabel3 = new CambioLabel();
    gbc.gridx =3;
    newButtonPanel.add(empLabel3,gbc);

    CambioLabel empLabel4 = new CambioLabel();
    gbc.gridx =4;
    newButtonPanel.add(empLabel4,gbc);


    CambioButton  newButton = new CambioButton("New");


    gbc.gridx=5;
    newButtonPanel.add(newButton,gbc);

    return newButtonPanel;
  }*/

  public void loadData()
  {
    contactSelectionComponent.setAllowNoneContact(false);
    contactSelectionComponent.setShowOnlyContactCombo(true);
    contactSelectionComponent.setPatientID(Framework.getInstance().getActiveSubjectOfCare().id);
    contactSelectionComponent.setAllowUnitAddMoreOptions(true);
    contactSelectionComponent.readContacts();

  }

  public void clearContactData()
  {
    contactSelectionComponent.setShowOnlyContactCombo(true);
    contactSelectionComponent.setPatientID(null);
    contactSelectionComponent.clearPanel();
  }
}
