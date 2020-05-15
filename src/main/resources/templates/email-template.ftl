<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Sending Email with Freemarker HTML Template Example</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>


    <style>
        body{
            margin: 0;
            padding: 0;
            font-family: Arial, sans-serif;
        }

        table{
            alignment: left;
            border: 0;
            collapse: 0;
            width: 600px;
            border-collapse: collapse;

        }
        .firstTr{
            height: 120px;
        }

        .first{
            alignment: center;
            background-color: #4184F3;
            padding: 0 0 5px 30px;
            text-align: left;
            vertical-align: bottom;
            color: #ffffff;
            font-size: 12px;
        }

        p{
            color: #330033;
        }

        h2{
            text-align: center;
        }
    </style>

</head>
<body>

<table>
    <tr class="firstTr" >
        <td class="first">
            <h1>CRS Verification Code</h1>
        </td>
    </tr>
    <tr>
        <td bgcolor="#fafafa" style="padding: 40px 30px 40px 30px;">
            <p>Dear ${name},</p>
            <p>CRS received a request to use this email address to help change your password.</p>
            <h2><b>${token}</b></h2>
            <p>Enter the code when asked for it to verify it’s you.</p>
            <p>If you ignore this message, your won't be able to change your password. If you didn't request a password reset, someone probably gave your email address by mistake. You can safely ignore this email.</p>
            <p>Sincerely,</p>
            <p>Alpha Team</p>
        </td>
    </tr>

</table>

</body>
</html>