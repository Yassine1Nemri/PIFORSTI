package com.melocode.videoismaael.Controller;

import com.melocode.videoismaael.Controller.UserC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import com.melocode.videoismaael.entities.User;
import com.melocode.videoismaael.services.UserService;
import org.jetbrains.annotations.NotNull;

import javax.accessibility.AccessibleContext;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.*;
import java.awt.im.InputContext;
import java.awt.im.InputMethodRequests;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.VolatileImage;
import java.beans.BeanProperty;
import java.beans.PropertyChangeListener;
import java.beans.Transient;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EventListener;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dashboard {
    @FXML
    private TextField searchField;
    @FXML
    private GridPane grid;
    @FXML
    private Pagination pagination;



    @FXML
    private Button deleteButton;
    // Total number of users
    private int totalUsers = 50 ;/* Get the total number of users */;

    // Number of users per page
    private int usersPerPage = 5;

    UserService us = new UserService();

    @FXML
    public void initialize() {
        int totalPages = (int) Math.ceil((double) totalUsers / usersPerPage);

        // Set the number of pages for pagination
        pagination.setPageCount(totalPages);

        // Set up the event handler for pagination
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            changePage(newIndex.intValue());

        });
        pagination.setCurrentPageIndex(0);
        // Display the initial page
        changePage(0);
        grid.getChildren().clear();
        displayg("");
    }
    private void changePage(int pageIndex) {
        // Calculate the start and end index of users for the current page
        int startIndex = pageIndex * usersPerPage;
        int endIndex = Math.min(startIndex + usersPerPage, totalUsers);

        // Clear existing content from the GridPane
        grid.getChildren().clear();

        // Populate the GridPane with users from startIndex to endIndex
        displayUsers(startIndex, endIndex);
    }
    private void displayUsers(int startIndex, int endIndex) {
        // Clear the GridPane
        grid.getChildren().clear();

        // Retrieve users from the database based on the start and end index
        ResultSet resultSet = us.getUsersInRange(startIndex, endIndex);

        try {
            int row = 0;
            int column = 0;

            // Set padding and margin for user cards
            Insets cardInsets = new Insets(5);
            int cardSpacing = 10;

            // Loop through the result set and populate the GridPane with user data
            while (resultSet.next()) {
                // Load the UserCard.fxml and controller
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/User.fxml"));
                AnchorPane userCard = loader.load();
                UserC controller = loader.getController();

                // Populate user data
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("roles"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("prenom"),
                        resultSet.getInt("tel"),
                        resultSet.getInt("is_banned")
                );

                System.out.println(user);

                // Set user data in the controller
                controller.setUser(user);


                // Add the user card to the GridPane
                grid.add(userCard, column++, row);

                // Increment row after adding user card
                if (column == 3) { // Adjust the column count as needed
                    column = 0;
                    row++;
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void refresh(ActionEvent event) {
        grid.getChildren().clear();
        displayg("");
    }



    @FXML
    void logout(ActionEvent event) throws IOException {
        System.out.println("logging out");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface.fxml"));
        Parent adminRoot = loader.load();

        Scene adminScene = new Scene(adminRoot);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(adminScene);
        window.show();
    }

    private void displayg(String searchText) {
        ///////////////////////////////////////////////////////////////
        ObservableList<User> l2 = FXCollections.observableArrayList();
        ResultSet resultSet2;
        if (searchText.isEmpty()) {
            resultSet2 = us.Getall(); // Retrieve all users if search text is empty
        } else {
            resultSet2 = us.searchUsers(searchText); // Retrieve filtered users based on search text
        }
        l2.clear();
        User pppp = new User();
        l2.add(pppp);
        int column = 0;
        int row = 2;
        if (l2.size() > 0) {

        }
        try {
            while (resultSet2.next()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/User.fxml"));
                try {
                    AnchorPane anchorPane = fxmlLoader.load();
                    UserC itemController = fxmlLoader.getController();
                    int id=resultSet2.getInt("id");
                    String mail=resultSet2.getString("email");
                    String pass=resultSet2.getString("password");
                    String ln=resultSet2.getString("name");
                    String fn=resultSet2.getString("prenom");
                    int tel=resultSet2.getInt("tel");
                    int isbanned= resultSet2.getInt("is_banned");
                    User ppppp = new User(id,mail,"",pass,ln,fn,tel,isbanned);
                    itemController.setData(ppppp);
                    if (column == 1) {
                        column = 0;
                        row++;
                    }
                    grid.add(anchorPane, column++, row); //(child,column,row)
                    //set grid width
                    grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                    grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    grid.setMaxWidth(Region.USE_PREF_SIZE);
                    //set grid height
                    grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                    grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    grid.setMaxHeight(Region.USE_PREF_SIZE);
                    GridPane.setMargin(anchorPane, new Insets(10));
                } catch (IOException ex) {
                    Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    @FXML
    private void search() {
        String searchText = searchField.getText().toLowerCase(); // Convert to lowercase for case-insensitive search

        // Clear the grid before populating with search results
        grid.getChildren().clear();

        // Call displayg() with the search text
        displayg(searchText);


    }

    private final UserService userService = new UserService();


    public void addNotify() {
        deleteButton.addNotify();
    }

    public boolean getFocusTraversalKeysEnabled() {
        return deleteButton.getFocusTraversalKeysEnabled();
    }

    public boolean isForegroundSet() {
        return deleteButton.isForegroundSet();
    }

    @Deprecated
    public Dimension minimumSize() {
        return deleteButton.minimumSize();
    }

    public String getActionCommand() {
        return deleteButton.getActionCommand();
    }

    public Point getLocation(Point rv) {
        return deleteButton.getLocation(rv);
    }

    @Deprecated
    public void disable() {
        deleteButton.disable();
    }

    public void paintAll(Graphics g) {
        deleteButton.paintAll(g);
    }

    public void removeMouseWheelListener(MouseWheelListener l) {
        deleteButton.removeMouseWheelListener(l);
    }

    @BeanProperty(expert = true, description = "The AccessibleContext associated with this Button.")
    public AccessibleContext getAccessibleContext() {
        return deleteButton.getAccessibleContext();
    }

    @Deprecated
    public void enable(boolean b) {
        deleteButton.enable(b);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        deleteButton.addPropertyChangeListener(listener);
    }

    public boolean isFontSet() {
        return deleteButton.isFontSet();
    }

    public void addHierarchyListener(HierarchyListener l) {
        deleteButton.addHierarchyListener(l);
    }

    public void setBounds(@NotNull Rectangle r) {
        deleteButton.setBounds(r);
    }

    @Deprecated
    public void move(int x, int y) {
        deleteButton.move(x, y);
    }

    public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
        return deleteButton.getListeners(listenerType);
    }

    public boolean isPreferredSizeSet() {
        return deleteButton.isPreferredSizeSet();
    }

    public void setCursor(Cursor cursor) {
        deleteButton.setCursor(cursor);
    }

    public void removeKeyListener(KeyListener l) {
        deleteButton.removeKeyListener(l);
    }

    @Deprecated
    public void show() {
        deleteButton.show();
    }

    public void setMinimumSize(Dimension minimumSize) {
        deleteButton.setMinimumSize(minimumSize);
    }

    public DropTarget getDropTarget() {
        return deleteButton.getDropTarget();
    }

    public void setComponentOrientation(ComponentOrientation o) {
        deleteButton.setComponentOrientation(o);
    }

    public void setActionCommand(String command) {
        deleteButton.setActionCommand(command);
    }

    public Dimension getSize(Dimension rv) {
        return deleteButton.getSize(rv);
    }

    public Point getMousePosition() throws HeadlessException {
        return deleteButton.getMousePosition();
    }

    public MouseListener[] getMouseListeners() {
        return deleteButton.getMouseListeners();
    }

    public void update(Graphics g) {
        deleteButton.update(g);
    }

    public void addKeyListener(KeyListener l) {
        deleteButton.addKeyListener(l);
    }

    public void addMouseWheelListener(MouseWheelListener l) {
        deleteButton.addMouseWheelListener(l);
    }

    public String getName() {
        return deleteButton.getName();
    }

    @Deprecated
    public boolean keyDown(Event evt, int key) {
        return deleteButton.keyDown(evt, key);
    }

    public void setPreferredSize(Dimension preferredSize) {
        deleteButton.setPreferredSize(preferredSize);
    }

    public void setFocusTraversalKeysEnabled(boolean focusTraversalKeysEnabled) {
        deleteButton.setFocusTraversalKeysEnabled(focusTraversalKeysEnabled);
    }

    public void setBounds(int x, int y, int width, int height) {
        deleteButton.setBounds(x, y, width, height);
    }

    public int getBaseline(int width, int height) {
        return deleteButton.getBaseline(width, height);
    }

    public FontMetrics getFontMetrics(Font font) {
        return deleteButton.getFontMetrics(font);
    }

    public boolean requestFocusInWindow() {
        return deleteButton.requestFocusInWindow();
    }

    public boolean prepareImage(Image image, ImageObserver observer) {
        return deleteButton.prepareImage(image, observer);
    }

    @Deprecated
    public boolean gotFocus(Event evt, Object what) {
        return deleteButton.gotFocus(evt, what);
    }

    public boolean isDisplayable() {
        return deleteButton.isDisplayable();
    }

    public void setLocation(int x, int y) {
        deleteButton.setLocation(x, y);
    }

    public void transferFocusBackward() {
        deleteButton.transferFocusBackward();
    }

    public ComponentListener[] getComponentListeners() {
        return deleteButton.getComponentListeners();
    }

    public int getWidth() {
        return deleteButton.getWidth();
    }

    public void repaint(int x, int y, int width, int height) {
        deleteButton.repaint(x, y, width, height);
    }

    @Deprecated
    public boolean mouseDown(Event evt, int x, int y) {
        return deleteButton.mouseDown(evt, x, y);
    }

    public boolean isFocusable() {
        return deleteButton.isFocusable();
    }

    public void setDropTarget(DropTarget dt) {
        deleteButton.setDropTarget(dt);
    }

    @Deprecated
    public boolean handleEvent(Event evt) {
        return deleteButton.handleEvent(evt);
    }

    public Object getTreeLock() {
        return deleteButton.getTreeLock();
    }

    public void paint(Graphics g) {
        deleteButton.paint(g);
    }

    @Deprecated
    public boolean inside(int x, int y) {
        return deleteButton.inside(x, y);
    }

    public void requestFocus(FocusEvent.Cause cause) {
        deleteButton.requestFocus(cause);
    }

    public void setLocale(Locale l) {
        deleteButton.setLocale(l);
    }

    public Dimension getMaximumSize() {
        return deleteButton.getMaximumSize();
    }

    @Deprecated
    public boolean lostFocus(Event evt, Object what) {
        return deleteButton.lostFocus(evt, what);
    }

    public boolean areFocusTraversalKeysSet(int id) {
        return deleteButton.areFocusTraversalKeysSet(id);
    }

    public void transferFocus() {
        deleteButton.transferFocus();
    }

    public void firePropertyChange(String propertyName, double oldValue, double newValue) {
        deleteButton.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void setLocation(@NotNull Point p) {
        deleteButton.setLocation(p);
    }

    @Deprecated
    public void enable() {
        deleteButton.enable();
    }

    @Deprecated
    public Rectangle bounds() {
        return deleteButton.bounds();
    }

    public boolean hasFocus() {
        return deleteButton.hasFocus();
    }

    public void removeHierarchyBoundsListener(HierarchyBoundsListener l) {
        deleteButton.removeHierarchyBoundsListener(l);
    }

    public boolean isValid() {
        return deleteButton.isValid();
    }

    @Deprecated
    public boolean mouseUp(Event evt, int x, int y) {
        return deleteButton.mouseUp(evt, x, y);
    }

    public Container getFocusCycleRootAncestor() {
        return deleteButton.getFocusCycleRootAncestor();
    }

    @Deprecated
    public Dimension preferredSize() {
        return deleteButton.preferredSize();
    }

    public Component.BaselineResizeBehavior getBaselineResizeBehavior() {
        return deleteButton.getBaselineResizeBehavior();
    }

    @Deprecated
    public boolean mouseEnter(Event evt, int x, int y) {
        return deleteButton.mouseEnter(evt, x, y);
    }

    public void repaint(long tm) {
        deleteButton.repaint(tm);
    }

    public boolean prepareImage(Image image, int width, int height, ImageObserver observer) {
        return deleteButton.prepareImage(image, width, height, observer);
    }

    public InputMethodListener[] getInputMethodListeners() {
        return deleteButton.getInputMethodListeners();
    }

    public void firePropertyChange(String propertyName, short oldValue, short newValue) {
        deleteButton.firePropertyChange(propertyName, oldValue, newValue);
    }

    public boolean contains(int x, int y) {
        return deleteButton.contains(x, y);
    }

    public void validate() {
        deleteButton.validate();
    }

    public void setBackground(Color c) {
        deleteButton.setBackground(c);
    }

    public void removeComponentListener(ComponentListener l) {
        deleteButton.removeComponentListener(l);
    }

    public Dimension getMinimumSize() {
        return deleteButton.getMinimumSize();
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        deleteButton.addPropertyChangeListener(propertyName, listener);
    }

    public void setForeground(Color c) {
        deleteButton.setForeground(c);
    }

    public ColorModel getColorModel() {
        return deleteButton.getColorModel();
    }

    @Deprecated
    public boolean keyUp(Event evt, int key) {
        return deleteButton.keyUp(evt, key);
    }

    public Dimension getSize() {
        return deleteButton.getSize();
    }

    public FocusListener[] getFocusListeners() {
        return deleteButton.getFocusListeners();
    }

    public HierarchyBoundsListener[] getHierarchyBoundsListeners() {
        return deleteButton.getHierarchyBoundsListeners();
    }

    public ActionListener[] getActionListeners() {
        return deleteButton.getActionListeners();
    }

    public void printAll(Graphics g) {
        deleteButton.printAll(g);
    }

    public Component getComponentAt(int x, int y) {
        return deleteButton.getComponentAt(x, y);
    }

    public void addHierarchyBoundsListener(HierarchyBoundsListener l) {
        deleteButton.addHierarchyBoundsListener(l);
    }

    public void setFont(Font f) {
        deleteButton.setFont(f);
    }

    public void addComponentListener(ComponentListener l) {
        deleteButton.addComponentListener(l);
    }

    public boolean isEnabled() {
        return deleteButton.isEnabled();
    }

    @Deprecated
    public boolean postEvent(Event e) {
        return deleteButton.postEvent(e);
    }

    public void list(PrintWriter out, int indent) {
        deleteButton.list(out, indent);
    }

    public void removeMouseListener(MouseListener l) {
        deleteButton.removeMouseListener(l);
    }

    public boolean isBackgroundSet() {
        return deleteButton.isBackgroundSet();
    }

    public void setFocusTraversalKeys(int id, Set<? extends AWTKeyStroke> keystrokes) {
        deleteButton.setFocusTraversalKeys(id, keystrokes);
    }

    public void setVisible(boolean b) {
        deleteButton.setVisible(b);
    }

    public void removeInputMethodListener(InputMethodListener l) {
        deleteButton.removeInputMethodListener(l);
    }

    public void setLabel(String label) {
        deleteButton.setLabel(label);
    }

    public Rectangle getBounds(Rectangle rv) {
        return deleteButton.getBounds(rv);
    }

    public void list(PrintStream out, int indent) {
        deleteButton.list(out, indent);
    }

    public void removeFocusListener(FocusListener l) {
        deleteButton.removeFocusListener(l);
    }

    public InputContext getInputContext() {
        return deleteButton.getInputContext();
    }

    @Deprecated
    public boolean isFocusTraversable() {
        return deleteButton.isFocusTraversable();
    }

    public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {
        deleteButton.firePropertyChange(propertyName, oldValue, newValue);
    }

    public boolean contains(Point p) {
        return deleteButton.contains(p);
    }

    public void setSize(int width, int height) {
        deleteButton.setSize(width, height);
    }

    public Image createImage(int width, int height) {
        return deleteButton.createImage(width, height);
    }

    public void removeActionListener(ActionListener l) {
        deleteButton.removeActionListener(l);
    }

    public void setName(String name) {
        deleteButton.setName(name);
    }

    public void print(Graphics g) {
        deleteButton.print(g);
    }

    @Deprecated
    public Component locate(int x, int y) {
        return deleteButton.locate(x, y);
    }

    public MouseMotionListener[] getMouseMotionListeners() {
        return deleteButton.getMouseMotionListeners();
    }

    public boolean isLightweight() {
        return deleteButton.isLightweight();
    }

    public void list(PrintWriter out) {
        deleteButton.list(out);
    }

    public void applyComponentOrientation(ComponentOrientation orientation) {
        deleteButton.applyComponentOrientation(orientation);
    }

    @Transient
    public boolean isVisible() {
        return deleteButton.isVisible();
    }

    public Set<AWTKeyStroke> getFocusTraversalKeys(int id) {
        return deleteButton.getFocusTraversalKeys(id);
    }

    public float getAlignmentY() {
        return deleteButton.getAlignmentY();
    }

    public void addInputMethodListener(InputMethodListener l) {
        deleteButton.addInputMethodListener(l);
    }

    public void removeNotify() {
        deleteButton.removeNotify();
    }

    public int getY() {
        return deleteButton.getY();
    }

    public boolean isShowing() {
        return deleteButton.isShowing();
    }

    public void list(PrintStream out) {
        deleteButton.list(out);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        deleteButton.removePropertyChangeListener(propertyName, listener);
    }

    @Deprecated
    public Point location() {
        return deleteButton.location();
    }

    public void setMixingCutoutShape(Shape shape) {
        deleteButton.setMixingCutoutShape(shape);
    }

    public void addMouseListener(MouseListener l) {
        deleteButton.addMouseListener(l);
    }

    @Deprecated
    public void layout() {
        deleteButton.layout();
    }

    public Cursor getCursor() {
        return deleteButton.getCursor();
    }

    public boolean isCursorSet() {
        return deleteButton.isCursorSet();
    }

    public void repaint(long tm, int x, int y, int width, int height) {
        deleteButton.repaint(tm, x, y, width, height);
    }

    public Image createImage(ImageProducer producer) {
        return deleteButton.createImage(producer);
    }

    public Point getLocationOnScreen() {
        return deleteButton.getLocationOnScreen();
    }

    public void enableInputMethods(boolean enable) {
        deleteButton.enableInputMethods(enable);
    }

    public void removeMouseMotionListener(MouseMotionListener l) {
        deleteButton.removeMouseMotionListener(l);
    }

    @Deprecated
    public boolean mouseExit(Event evt, int x, int y) {
        return deleteButton.mouseExit(evt, x, y);
    }

    public void addActionListener(ActionListener l) {
        deleteButton.addActionListener(l);
    }

    public void addFocusListener(FocusListener l) {
        deleteButton.addFocusListener(l);
    }

    public void requestFocus() {
        deleteButton.requestFocus();
    }

    public boolean isMaximumSizeSet() {
        return deleteButton.isMaximumSizeSet();
    }

    public void revalidate() {
        deleteButton.revalidate();
    }

    public VolatileImage createVolatileImage(int width, int height) {
        return deleteButton.createVolatileImage(width, height);
    }

    public Container getParent() {
        return deleteButton.getParent();
    }

    @Transient
    public Color getForeground() {
        return deleteButton.getForeground();
    }

    public void firePropertyChange(String propertyName, long oldValue, long newValue) {
        deleteButton.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void setEnabled(boolean b) {
        deleteButton.setEnabled(b);
    }

    public Dimension getPreferredSize() {
        return deleteButton.getPreferredSize();
    }

    public void repaint() {
        deleteButton.repaint();
    }

    public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
        return deleteButton.imageUpdate(img, infoflags, x, y, w, h);
    }

    public boolean getIgnoreRepaint() {
        return deleteButton.getIgnoreRepaint();
    }

    public void dispatchEvent(AWTEvent e) {
        deleteButton.dispatchEvent(e);
    }

    @Deprecated
    public void deliverEvent(Event e) {
        deleteButton.deliverEvent(e);
    }

    @Deprecated
    public boolean action(Event evt, Object what) {
        return deleteButton.action(evt, what);
    }

    public Toolkit getToolkit() {
        return deleteButton.getToolkit();
    }

    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        return deleteButton.getPropertyChangeListeners(propertyName);
    }

    public void transferFocusUpCycle() {
        deleteButton.transferFocusUpCycle();
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        return deleteButton.getPropertyChangeListeners();
    }

    public void invalidate() {
        deleteButton.invalidate();
    }

    public int checkImage(Image image, ImageObserver observer) {
        return deleteButton.checkImage(image, observer);
    }

    public boolean isDoubleBuffered() {
        return deleteButton.isDoubleBuffered();
    }

    public Point getLocation() {
        return deleteButton.getLocation();
    }

    public void setMaximumSize(Dimension maximumSize) {
        deleteButton.setMaximumSize(maximumSize);
    }

    public void addMouseMotionListener(MouseMotionListener l) {
        deleteButton.addMouseMotionListener(l);
    }

    @Deprecated
    public Dimension size() {
        return deleteButton.size();
    }

    @Deprecated
    public boolean mouseDrag(Event evt, int x, int y) {
        return deleteButton.mouseDrag(evt, x, y);
    }

    public VolatileImage createVolatileImage(int width, int height, ImageCapabilities caps) throws AWTException {
        return deleteButton.createVolatileImage(width, height, caps);
    }

    public void remove(MenuComponent popup) {
        deleteButton.remove(popup);
    }

    public Component getComponentAt(Point p) {
        return deleteButton.getComponentAt(p);
    }

    public HierarchyListener[] getHierarchyListeners() {
        return deleteButton.getHierarchyListeners();
    }

    public boolean requestFocusInWindow(FocusEvent.Cause cause) {
        return deleteButton.requestFocusInWindow(cause);
    }

    @Deprecated
    public void reshape(int x, int y, int width, int height) {
        deleteButton.reshape(x, y, width, height);
    }

    public String getLabel() {
        return deleteButton.getLabel();
    }

    @Deprecated
    public void show(boolean b) {
        deleteButton.show(b);
    }

    @Transient
    public Font getFont() {
        return deleteButton.getFont();
    }

    public void setSize(@NotNull Dimension d) {
        deleteButton.setSize(d);
    }

    public void setIgnoreRepaint(boolean ignoreRepaint) {
        deleteButton.setIgnoreRepaint(ignoreRepaint);
    }

    public int getHeight() {
        return deleteButton.getHeight();
    }

    @Deprecated
    public boolean mouseMove(Event evt, int x, int y) {
        return deleteButton.mouseMove(evt, x, y);
    }

    @Transient
    public Color getBackground() {
        return deleteButton.getBackground();
    }

    @Deprecated
    public void resize(int width, int height) {
        deleteButton.resize(width, height);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        deleteButton.removePropertyChangeListener(listener);
    }

    public InputMethodRequests getInputMethodRequests() {
        return deleteButton.getInputMethodRequests();
    }

    public void setFocusable(boolean focusable) {
        deleteButton.setFocusable(focusable);
    }

    public void list() {
        deleteButton.list();
    }

    public int checkImage(Image image, int width, int height, ImageObserver observer) {
        return deleteButton.checkImage(image, width, height, observer);
    }

    public boolean isOpaque() {
        return deleteButton.isOpaque();
    }

    public void add(PopupMenu popup) {
        deleteButton.add(popup);
    }

    public void firePropertyChange(String propertyName, float oldValue, float newValue) {
        deleteButton.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void removeHierarchyListener(HierarchyListener l) {
        deleteButton.removeHierarchyListener(l);
    }

    @Deprecated
    public void hide() {
        deleteButton.hide();
    }

    public Locale getLocale() {
        return deleteButton.getLocale();
    }

    @Deprecated
    public void nextFocus() {
        deleteButton.nextFocus();
    }

    public float getAlignmentX() {
        return deleteButton.getAlignmentX();
    }

    public Graphics getGraphics() {
        return deleteButton.getGraphics();
    }

    public boolean isFocusOwner() {
        return deleteButton.isFocusOwner();
    }

    public ComponentOrientation getComponentOrientation() {
        return deleteButton.getComponentOrientation();
    }

    public KeyListener[] getKeyListeners() {
        return deleteButton.getKeyListeners();
    }

    public Rectangle getBounds() {
        return deleteButton.getBounds();
    }

    public MouseWheelListener[] getMouseWheelListeners() {
        return deleteButton.getMouseWheelListeners();
    }

    public int getX() {
        return deleteButton.getX();
    }

    public boolean isFocusCycleRoot(Container container) {
        return deleteButton.isFocusCycleRoot(container);
    }

    @Deprecated
    public void resize(@NotNull Dimension d) {
        deleteButton.resize(d);
    }

    public GraphicsConfiguration getGraphicsConfiguration() {
        return deleteButton.getGraphicsConfiguration();
    }

    public void doLayout() {
        deleteButton.doLayout();
    }

    public void firePropertyChange(String propertyName, char oldValue, char newValue) {
        deleteButton.firePropertyChange(propertyName, oldValue, newValue);
    }

    public boolean isMinimumSizeSet() {
        return deleteButton.isMinimumSizeSet();
    }



}
