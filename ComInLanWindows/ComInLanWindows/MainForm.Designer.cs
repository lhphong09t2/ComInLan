namespace ComInLanWindows
{
    partial class MainForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
			this.startButton = new System.Windows.Forms.Button();
			this.inputText = new System.Windows.Forms.TextBox();
			this.nameLabel = new System.Windows.Forms.Label();
			this.resultLabel = new System.Windows.Forms.Label();
			this.outputText = new System.Windows.Forms.TextBox();
			this.listenAtPort = new System.Windows.Forms.Label();
			this.idText = new System.Windows.Forms.TextBox();
			this.idLabel = new System.Windows.Forms.Label();
			this.changIdButton = new System.Windows.Forms.Button();
			this.SuspendLayout();
			// 
			// startButton
			// 
			this.startButton.Location = new System.Drawing.Point(12, 273);
			this.startButton.Name = "startButton";
			this.startButton.Size = new System.Drawing.Size(75, 23);
			this.startButton.TabIndex = 0;
			this.startButton.Text = "Start";
			this.startButton.UseVisualStyleBackColor = true;
			this.startButton.Click += new System.EventHandler(this.startButton_Click);
			// 
			// inputText
			// 
			this.inputText.Location = new System.Drawing.Point(54, 12);
			this.inputText.Name = "inputText";
			this.inputText.Size = new System.Drawing.Size(325, 20);
			this.inputText.TabIndex = 1;
			this.inputText.TextChanged += new System.EventHandler(this.inputText_TextChanged);
			// 
			// nameLabel
			// 
			this.nameLabel.AutoSize = true;
			this.nameLabel.Location = new System.Drawing.Point(14, 12);
			this.nameLabel.Name = "nameLabel";
			this.nameLabel.Size = new System.Drawing.Size(35, 13);
			this.nameLabel.TabIndex = 2;
			this.nameLabel.Text = "Name";
			// 
			// resultLabel
			// 
			this.resultLabel.AutoSize = true;
			this.resultLabel.Location = new System.Drawing.Point(12, 83);
			this.resultLabel.Name = "resultLabel";
			this.resultLabel.Size = new System.Drawing.Size(37, 13);
			this.resultLabel.TabIndex = 2;
			this.resultLabel.Text = "Result";
			// 
			// outputText
			// 
			this.outputText.Enabled = false;
			this.outputText.Location = new System.Drawing.Point(12, 99);
			this.outputText.Multiline = true;
			this.outputText.Name = "outputText";
			this.outputText.Size = new System.Drawing.Size(367, 168);
			this.outputText.TabIndex = 1;
			this.outputText.TextChanged += new System.EventHandler(this.inputText_TextChanged);
			// 
			// listenAtPort
			// 
			this.listenAtPort.AutoSize = true;
			this.listenAtPort.Location = new System.Drawing.Point(108, 278);
			this.listenAtPort.Name = "listenAtPort";
			this.listenAtPort.Size = new System.Drawing.Size(56, 13);
			this.listenAtPort.TabIndex = 3;
			this.listenAtPort.Text = "Listen at --";
			// 
			// idText
			// 
			this.idText.Enabled = false;
			this.idText.Location = new System.Drawing.Point(54, 46);
			this.idText.Name = "idText";
			this.idText.Size = new System.Drawing.Size(325, 20);
			this.idText.TabIndex = 1;
			this.idText.TextChanged += new System.EventHandler(this.inputText_TextChanged);
			// 
			// idLabel
			// 
			this.idLabel.AutoSize = true;
			this.idLabel.Location = new System.Drawing.Point(12, 49);
			this.idLabel.Name = "idLabel";
			this.idLabel.Size = new System.Drawing.Size(18, 13);
			this.idLabel.TabIndex = 2;
			this.idLabel.Text = "ID";
			// 
			// changIdButton
			// 
			this.changIdButton.Location = new System.Drawing.Point(304, 273);
			this.changIdButton.Name = "changIdButton";
			this.changIdButton.Size = new System.Drawing.Size(75, 23);
			this.changIdButton.TabIndex = 0;
			this.changIdButton.Text = "Chang ID";
			this.changIdButton.UseVisualStyleBackColor = true;
			this.changIdButton.Click += new System.EventHandler(this.changIdButton_Click);
			// 
			// MainForm
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.ClientSize = new System.Drawing.Size(391, 302);
			this.Controls.Add(this.listenAtPort);
			this.Controls.Add(this.resultLabel);
			this.Controls.Add(this.idLabel);
			this.Controls.Add(this.nameLabel);
			this.Controls.Add(this.outputText);
			this.Controls.Add(this.idText);
			this.Controls.Add(this.inputText);
			this.Controls.Add(this.changIdButton);
			this.Controls.Add(this.startButton);
			this.Name = "MainForm";
			this.Text = "Main";
			this.ResumeLayout(false);
			this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button startButton;
        private System.Windows.Forms.TextBox inputText;
        private System.Windows.Forms.Label nameLabel;
        private System.Windows.Forms.Label resultLabel;
        private System.Windows.Forms.TextBox outputText;
        private System.Windows.Forms.Label listenAtPort;
        private System.Windows.Forms.TextBox idText;
        private System.Windows.Forms.Label idLabel;
        private System.Windows.Forms.Button changIdButton;
    }
}

