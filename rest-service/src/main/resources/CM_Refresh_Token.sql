SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[CM_Refresh_Token](
    [ID] [bigint] IDENTITY(1,1) NOT NULL,
    [ExpiryDate] [datetime] NULL,
    [RefreshToken] [varchar](255) NULL,
    [UserPhone] [varchar](20) NULL,
    [IPAddress] [varchar](50) NULL,
    [PrgKind] [varchar](10) NULL,
    [OSKind] [varchar](10) NULL,

    PRIMARY KEY CLUSTERED
(
[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
    ) ON [PRIMARY]
    GO

ALTER TABLE [dbo].[CM_Refresh_Token]  WITH CHECK ADD  CONSTRAINT [FK_CM_Refresh_Token_UserId_CM_Users_UserID] FOREIGN KEY([UserID])
    REFERENCES [dbo].[CM_Users] ([UserID])
    GO

ALTER TABLE [dbo].[CM_Refresh_Token] CHECK CONSTRAINT [FK_CM_Refresh_Token_UserId_CM_Users_UserID]
    GO
