package org.sonatype.siesta.webapp.test;

/**
 * Test {@link Exception}.
 *
 * @since 2.0
 */
public class TestException
  extends Exception
{
  public TestException() {
    super();
  }

  public TestException(final String message) {
    super(message);
  }

  public TestException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public TestException(final Throwable cause) {
    super(cause);
  }
}
