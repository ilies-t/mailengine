package com.fishemi.mailengine.constant;

public class MailButtonConstant {

  private MailButtonConstant() {
    throw new IllegalStateException("Constant class, cannot be instantiated");
  }

  public static final String MICROSOFT = """
        <a
          data-linkindex="0" rel="noopener noreferrer"
          style="display:inline-block; background:#2172b9; color:white; font-family:Helvetica,Arial,sans-serif; font-size:14px; font-weight:600; margin:0; text-decoration:none; text-transform:none; padding:0px 25px 10px 25px; border-radius:0"
          href="%s"
        >
          Modifier votre mot de passe
        </a>
        """;

  public static final String GOOGLE = """
        <div style="padding-top:25px;text-align:center">
          <a
            style="font-family:'Google Sans',Roboto,RobotoDraft,Helvetica,Arial,sans-serif;line-height:16px;color:#ffffff;font-weight:400;text-decoration:none;font-size:14px;display:inline-block;padding:10px 24px;background-color:#4184f3;border-radius:5px;min-width:90px"
            target="_blank"
            href="%s"
          >
            Modifier votre mot de passe
          </a>
        </div>
    """;

  public static final String PLAIN = """
        <a href="%s" style="display:block;background-color:#007BFF;color:#ffffff;width:fit-content;text-decoration:none;border-radius:5px;font-size:16px;white-space:nowrap;padding:10px 20px;"
        >
          Réinitialiser le mot de passe
        </a>
    """;
}
