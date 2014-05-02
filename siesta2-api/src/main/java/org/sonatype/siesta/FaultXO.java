package org.sonatype.siesta;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Fault exchange object.
 *
 * @since 2.0
 */
@XmlRootElement(name = "fault")
public class FaultXO
{
  private String id;

  private String message;

  public FaultXO() {
    super();
  }

  public FaultXO(final String id, final String message) {
    this.id = id;
    this.message = message;
  }

  public FaultXO(final String id, final Throwable cause) {
    this(id, cause.toString());
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{" +
        "id='" + id + '\'' +
        ", message='" + message + '\'' +
        '}';
  }
}
