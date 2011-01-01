package zorglux.inominax.client;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InominaxServiceAsync {

   // token set management
   public void getTokenSetsNames(AsyncCallback<List<String>> callback);
   public void createTokenSet(String name, AsyncCallback<Void> callback);
   public void removeTokenSet(String name, AsyncCallback<Void> callback);
   void renameTokenSet(String oldName, String newName, AsyncCallback<Void> callback);

   // token management
   public void getTokensOfSet(String name, AsyncCallback<Set<String>> callback);
   public void addToTokenSet(String tokenSetName, String[] tokens, AsyncCallback<Void> callback);
   public void removeFromTokenSet(String tokenSetName, String[] tokens, AsyncCallback<Void> callback);

}
