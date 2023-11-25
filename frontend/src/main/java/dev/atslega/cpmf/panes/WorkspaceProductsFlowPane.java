package dev.atslega.cpmf.panes;

import dev.atslega.cpmf.workspace.WorkspaceProducts;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

public class WorkspaceProductsFlowPane extends FlowPane {
    private static final int PRODUCT_SIZE = 200;
    private static final int GAP_SIZE = 10;

    private WorkspaceProducts workspaceProducts;
    private PauseTransition pauseTransition;

    public WorkspaceProductsFlowPane(WorkspaceProducts workspaceProducts) {
        this.workspaceProducts = workspaceProducts;

        setPadding(new Insets(GAP_SIZE));
        setHgap(GAP_SIZE);
        setVgap(GAP_SIZE);
        setMaxHeight(Double.MAX_VALUE);

        setStyle("-fx-background-color: #26262B");

        pauseTransition = new PauseTransition(Duration.seconds(0.1));
        pauseTransition.setOnFinished(event -> updateProducts());

        widthProperty().addListener(e -> restartCooldown());
        heightProperty().addListener(e -> restartCooldown());
    }

    private void restartCooldown() {
        pauseTransition.stop();
        pauseTransition.playFromStart();
    }

    private void updateProducts() {
        int maxProductsPerPage = calculateMaxProducts(getWidth(), getHeight());

        int currentPage = workspaceProducts.getCurrentPage();
        int totalProducts = workspaceProducts.getTotalProducts();

        int start = (currentPage - 1) * maxProductsPerPage;
        int end = Math.min(start + maxProductsPerPage, totalProducts);

        getChildren().clear();
        for (int i = start; i < end; i++) {
            getChildren().add(createProductPane(i));
        }

        int totalPages = (int) Math.ceil((double) totalProducts / maxProductsPerPage);
        workspaceProducts.setTotalPages(totalPages);

        workspaceProducts.getBtnBack().setDisable(currentPage <= 1);
        workspaceProducts.getBtnNext().setDisable(currentPage >= totalPages);
    }

    private int calculateMaxProducts(double width, double height) {
        int columns = Math.max(1, (int) (width - GAP_SIZE) / (PRODUCT_SIZE + GAP_SIZE));
        int rows = Math.max(1, (int) (height - GAP_SIZE*2) / (PRODUCT_SIZE + GAP_SIZE));

        return columns * rows;
    }


    private FlowPane createProductPane(int id){
        FlowPane productPane = new FlowPane();
        productPane.setPrefSize(PRODUCT_SIZE, PRODUCT_SIZE);
        productPane.setStyle("-fx-background-color: #18191C; -fx-background-radius: 5;");
        productPane.setPadding(new Insets(10));

        Label label = new Label("Produkt ID " + id);
        label.setStyle("-fx-text-fill: white;"); // Set label text color to white

        productPane.getChildren().add(label);

        return productPane;
    }
}