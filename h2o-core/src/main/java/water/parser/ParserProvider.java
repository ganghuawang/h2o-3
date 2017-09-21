package water.parser;

import water.Job;
import water.Key;
import water.fvec.ByteVec;
import water.fvec.Vec;

/**
 * Generic Parser provider.
 */
public abstract class ParserProvider {
  /** Technical information for this parser */
  public abstract ParserInfo info();

  /** Create a new parser
   */
  public abstract Parser createParser(ParseSetup setup, Key<Job> jobKey);

  public final ParseSetup guessSetup(ByteVec v, byte[] bits, ParseSetup userSetup) {
    return guessSetup_impl(v, bits, userSetup);
  }

  protected ParseSetup guessSetup_impl(ByteVec v, byte[] bits, ParseSetup userSetup) {
    ParseSetup ps = guessFormatSetup(v, bits, userSetup);
    return guessDataSetup(v, bits, ps);
  }

  public ParseSetup guessFormatSetup(ByteVec v, byte[] bits, ParseSetup userSetup) {
    return userSetup;
  }

  public ParseSetup guessDataSetup(ByteVec v, byte[] bits, ParseSetup ps) {
    return guessSetup(v, bits, ps._separator, ps._number_columns, ps._single_quotes,
            ps._check_header, ps._column_names, ps._column_types, ps._domains, ps._na_strings);
  }

  /** Returns parser setup of throws exception if input is not recognized */
  // FIXME: should be more flexible
  public ParseSetup guessSetup(ByteVec v, byte[] bits, byte sep, int ncols, boolean singleQuotes, int checkHeader, String[] columnNames, byte[] columnTypes, String[][] domains, String[][] naStrings) {
    throw new UnsupportedOperationException("Not implemented. This method is kept only for backwards compatibility. " +
            "Override methods guessFormatSetup & guessDataSetup if you are implementing a new parser.");
  }

  /** Create a parser specific setup.
   *
   * Useful if parser need a single
   * @param inputs  input keys
   * @param requiredSetup  user given parser setup
   * @return  parser specific setup
   */
  public abstract ParseSetup createParserSetup(Key[] inputs, ParseSetup requiredSetup);

  /**
   * Executed exactly once per-file-per-node during parse.
   * Do any file-related non-distributed setup here. E.g. ORC reader creates node-shared instance of a (non-serializable) Reader.
   * @param v
   * @param setup
   */

  public ParseSetup setupLocal(Vec v, ParseSetup setup){ return setup;}
}
