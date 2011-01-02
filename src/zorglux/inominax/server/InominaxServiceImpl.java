package zorglux.inominax.server;

import static zorglux.inominax.exception.FunctionnalException.throwFunctionnalExceptionIfFalse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import zorglux.inominax.client.InominaxService;
import zorglux.inominax.shared.TokenSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class InominaxServiceImpl extends RemoteServiceServlet implements InominaxService {

   private List<TokenSet> tokenSets = null; // TODO TokenSetRepository

   private void initDefaultTokenSets() {
      TokenSet elvesTokenset = new TokenSet("Elfe");
      elvesTokenset.addToken("lae", "il", "mar", "sel", "fel", "fin", "iel", "gad", "del", "sin", "rin", "las", "gal", "ald", "ael", "din", "jad", "el", "ga", "la", "dri", "el", "ol");
      TokenSet dwarfTokenSet = new TokenSet("Nain");
      dwarfTokenSet.addToken("zak", "zok", "zek", "kar", "kor", "rok", "rak", "grim", "rek", "gra", "gru", "gre", "drak", "dak", "da", "du", "do", "gur", "hel", "ga", "gu", "go", "re", "ra", "ro", "bal", "bol", "ba", "bo", "bar", "bor", "bur", "son", "gir");
      TokenSet humanTokenSet = new TokenSet("Humain");
      humanTokenSet.addToken("bo", "ris", "ma", "ri", "drak", "jo", "kim", "joa", "mir", "ro", "a", "ra", "gorn", "sel", "rik", "drik", "jon", "gal", "bal", "bol", "ba", "bo", "del", "sin", "rin");
      tokenSets = new ArrayList<TokenSet>();
      tokenSets.add(elvesTokenset);
      tokenSets.add(dwarfTokenSet);
      tokenSets.add(humanTokenSet);
   }

   @Override
   public List<String> getTokenSetsNames() {
      List<TokenSet> allTokenSets = getAllTokenSets();
      List<String> tokenSetsNames = new ArrayList<String>(allTokenSets.size());
      for (TokenSet tokenSet : allTokenSets) {
         tokenSetsNames.add(tokenSet.getName());
      }
      Collections.sort(tokenSetsNames);
      return tokenSetsNames;
   }

   @Override
   public TokenSet createTokenSet(String tokenSetName) {
      throwFunctionnalExceptionIfFalse(checkTokenSetNameIsAvailable(tokenSetName), "name already used : " + tokenSetName);
      TokenSet newTokenSet = new TokenSet(tokenSetName);
      tokenSets.add(newTokenSet);
      GWT.log("create token set " + tokenSetName);
      return newTokenSet;
   }

   @Override
   public void removeTokenSet(String name) {
      tokenSets.remove(findTokenSetByName(name));
   }

   // token management
   @Override
   public Set<String> getTokensOfSet(String name) {
      if (tokenSetExists(name)) { return findTokenSetByName(name).getTokens(); }
      // return empty set if we can't find a tokenset corresponding to the given name
      return new HashSet<String>();
   }

   private TokenSet findTokenSetByName(String name) {
      for (TokenSet tokenSet : tokenSets) {
         if (tokenSet.getName().equals(name)) { return tokenSet; }
      }
      // return null if we can't find a tokenset corresponding to the given name
      return null;
   }

   @Override
   public void addToTokenSet(String tokenSetName, String... tokens) {
      if (tokenSetExists(tokenSetName)) {
         findTokenSetByName(tokenSetName).addToken(tokens);
      }
   }

   @Override
   public void removeFromTokenSet(String tokenSetName, String... tokens) {
      if (tokenSetExists(tokenSetName)) {
         GWT.log("removing tokens " + tokens + " from " + tokenSetName);
         findTokenSetByName(tokenSetName).removeTokens(tokens);
      }
   }

   private List<TokenSet> getAllTokenSets() {
      if (tokenSets == null) {
         initDefaultTokenSets();
      }
      return tokenSets;
   }

   public boolean checkTokenSetNameIsAvailable(String tokenSetName) {
      return !tokenSetExists(tokenSetName);
   }

   private boolean tokenSetExists(String tokenSetName) {
      return getTokenSetsNames().contains(tokenSetName);
   }

   @Override
   public void renameTokenSet(String oldName, String newName) {
      throwFunctionnalExceptionIfFalse(checkTokenSetNameIsAvailable(newName), "name already used : " + newName);
      throwFunctionnalExceptionIfFalse(tokenSetExists(oldName), "no token list with name : " + oldName);
      findTokenSetByName(oldName).setName(newName);
      // TODO : persist renamed TokenSet ...
   }

   @Override
   public void cloneTokenSet(String nameOfTokenSetToClone, String newTokenSetName) {
      throwFunctionnalExceptionIfFalse(checkTokenSetNameIsAvailable(newTokenSetName), "name already used : " + newTokenSetName);
      throwFunctionnalExceptionIfFalse(tokenSetExists(nameOfTokenSetToClone), "no token list with name : " + nameOfTokenSetToClone);
      // everything should be ok to clone a token set
      TokenSet clone = createTokenSet(newTokenSetName);
      TokenSet tokenSetToClone = findTokenSetByName(nameOfTokenSetToClone);
      clone.getTokens().addAll(tokenSetToClone.getTokens());
      // TODO : persist clone ...
   }

}
