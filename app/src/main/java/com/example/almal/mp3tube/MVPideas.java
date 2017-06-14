package com.example.almal.mp3tube;

/**
 * Created by almal on 2017-06-13.
 */

public class MVPideas {

        public interface View {

            void showAddProductForm();
/*
            void showEditProductForm(Product product);

            void showDeleteProductPrompt(Product product);

            void showGoogleSearch(Product product);*/

            void showEmptyText();

            void hideEmptyText();

            void showMessage(String message);

            void showPlayer();
            void  showPlayButton();

        }

        public interface Actions {
            void loadProducts();

            void onAddProductButtonClicked();
/*
            void onAddToCartButtonClicked(Product product);

            Product getProduct(long id);

            void addProduct(Product product);

            void onDeleteProductButtonClicked(Product product);

            void deleteProduct(Product product);

            void onEditProductButtonClicked(Product product);

            void updateProduct(Product product);*/

            void play();
            void stop();

        }

        public interface Repository {
/*
            Product getProductById(long id);

            void deleteProduct(Product product);

            void addProduct(Product product);

            void updateProduct(Product product);*/
            void addToHistory();


        }
}
