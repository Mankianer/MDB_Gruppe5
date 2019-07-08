(defun vpn-fb4-connect ()
  (interactive)
  (start-process-shell-command
   "fb4-vpn"
   "*vpn*"
   (concat "ikec -r fb4 -u tieic002 "
           "-p "
           ;; Ruft die Funktion auf, die das Passwort fuer die Fh-Dortmund ausgibt
           (funcall (let ((auth-sources '(macos-keychain-generic)))
                      (plist-get (nth 0
                                      (auth-source-search :max 1 :user "tieic002" :label "fh-dortmund")) :secret)))
           " -a")))

(defun toggle-vpn-fb (universal)
  "Stellt eine VPN Verbindung zum FB4 her, wenn keine besteht. Besteht eine so wird diese beendet"
  (interactive "P")
  (let ((status (process-status "fb4-vpn")))
    (cond
     ((eq status 'run) (kill-process "fb4-vpn"))
     (t (vpn-fb4-connect)))))

